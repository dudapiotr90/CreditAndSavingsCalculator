package pl.zajavka.bankCalculator.creditCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.MortgageException;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.ReferenceCalculationService;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.ReferenceCalculationServiceImpl;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class ReferenceCalculationServiceImplTest {

    ReferenceCalculationService referenceCalculationService;

    public static Stream<Arguments> testReferenceDataForFirstRate() {
        return Stream.of(
            arguments(
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(0),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                Overpayment.REDUCE_RATE
            ),
            arguments(
                BigDecimal.valueOf(112133),
                BigDecimal.valueOf(180),
                BigDecimal.valueOf(12412),
                BigDecimal.valueOf(124545),
                Overpayment.REDUCE_RATE
            ),
            arguments(
                BigDecimal.valueOf(124545),
                BigDecimal.valueOf(180),
                BigDecimal.valueOf(12412),
                BigDecimal.valueOf(124545),
                Overpayment.REDUCE_PERIOD
            )
        );

    }

    public static Stream<Arguments> testReferenceDataForOtherRates() {
        return Stream.of(
            arguments(
                BigDecimal.valueOf(110566.45),
                BigDecimal.valueOf(59.0),
                BigDecimal.valueOf(12471.47),
                Overpayment.REDUCE_RATE,
                BigDecimal.valueOf(123647.45)
            ),
            arguments(
                BigDecimal.valueOf(140000),
                BigDecimal.valueOf(180),
                BigDecimal.valueOf(12471.47),
                Overpayment.REDUCE_PERIOD,
                BigDecimal.valueOf(6479.12)
            ),
            arguments(
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(1456.47),
                Overpayment.REDUCE_RATE,
                BigDecimal.ZERO
            ),
            arguments(
                BigDecimal.valueOf(150213.67),
                BigDecimal.valueOf(120),
                BigDecimal.valueOf(0),
                Overpayment.REDUCE_RATE,
                BigDecimal.valueOf(14774.41)
            )
        );
    }

    @BeforeEach
    void setUp() {
        this.referenceCalculationService = new ReferenceCalculationServiceImpl();
    }

    @ParameterizedTest
    @MethodSource(value = "testReferenceDataForFirstRate")
    void shouldCalculateMortgageReferenceForFirstRateCorrectly(
        BigDecimal expectedAmount,
        BigDecimal expectedDuration,
        BigDecimal overpaymentAmount,
        BigDecimal mortgageAmount,
        String overpaymentReduceWay
    ) {
        //given
        MortgageData mortgageData = TestMortgageData.someInputData()
            .withAmount(mortgageAmount)
            .withOverpaymentReduceWay(overpaymentReduceWay);
        Overpayment overpayment = TestMortgageData.someOverpayment()
            .withAmount(overpaymentAmount);

        MortgageReference expected = new MortgageReference(
            expectedAmount,
            expectedDuration
        );

        //when
        MortgageReference result = referenceCalculationService.calculate(mortgageData, overpayment);


        //then
        Assertions.assertEquals(expected, result);

    }

    @ParameterizedTest
    @MethodSource(value = "testReferenceDataForOtherRates")
    void shouldCalculateMortgageReferenceForOtherRatesCorrectly(
        BigDecimal expectedAmount,
        BigDecimal expectedDuration,
        BigDecimal overpaymentAmount,
        String overpaymentReduceWay,
        BigDecimal residualAmount
    ) {
        //given
        MortgageData mortgageData = TestMortgageData.someInputData()
            .withOverpaymentReduceWay(overpaymentReduceWay);

        RateAmounts rateAmounts = TestMortgageData.someRateAmounts()
            .withOverpayment(TestMortgageData.someOverpayment()
                .withAmount(overpaymentAmount));

        Rate rate = TestMortgageData.someRate()
            .withMortgageResidual(TestMortgageData.someMortgageResidual()
                .withAmount(residualAmount));

        MortgageReference expected = new MortgageReference(expectedAmount, expectedDuration);

        //when
        MortgageReference result = referenceCalculationService.calculate(mortgageData, rateAmounts, rate);

        //then
        Assertions.assertEquals(expected, result);

    }

    @Test
    void shouldThrowMortgageExceptionCorrectly() {
        //given
        MortgageData mortgageData = TestMortgageData.someInputData().withOverpaymentReduceWay("Changeable");
        Rate rate = TestMortgageData.someRate();
        //when
        Throwable exception = Assertions.assertThrows(MortgageException.class,
            () -> referenceCalculationService.calculate(mortgageData, null, rate));
        //then
        Assertions.assertEquals("Case not handled", exception.getMessage());
    }

}