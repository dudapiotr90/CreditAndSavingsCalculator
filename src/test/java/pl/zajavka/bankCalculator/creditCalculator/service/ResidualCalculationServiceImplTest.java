package pl.zajavka.bankCalculator.creditCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResidualCalculationServiceImplTest {

    ResidualCalculationService residualCalculationService;

    @BeforeEach
    void setUp() {
        this.residualCalculationService = new ResidualCalculationServiceImpl();
    }

    @Test
    void shouldCalculateResidualForFirstRateCorrectly() {
        //given
        InputData inputData1 = TestMortgageData.someInputData();
        InputData inputData2 = TestMortgageData.someInputData()
            .withAmount(BigDecimal.ZERO);
        RateAmounts rateAmounts = TestMortgageData.someRateAmounts()
            .withOverpayment(TestMortgageData.someOverpayment());

        MortgageResidual expected1 = TestMortgageData.someMortgageResidual()
            .withDuration(BigDecimal.valueOf(179))
            .withAmount(BigDecimal.valueOf(139390.47));

        MortgageResidual expected2 = TestMortgageData.someMortgageResidual()
            .withDuration(BigDecimal.valueOf(0))
            .withAmount(BigDecimal.valueOf(0));


        //when
        MortgageResidual result1 = residualCalculationService.calculate(rateAmounts, inputData1);
        MortgageResidual result2 = residualCalculationService.calculate(rateAmounts, inputData2);

        //then
        Assertions.assertEquals(expected1, result1);
        Assertions.assertEquals(expected2, result2);


    }

    @ParameterizedTest
    @MethodSource(value = "testResidualData")
    void shouldCalculateMortgageResidualForOtherRatesCorrectly(
        BigDecimal expectedAmount,
        BigDecimal expectedDuration,
        BigDecimal mortgageAmount,
        BigDecimal previousResidualAmount,
        BigDecimal previousResidualDuration,
        RateTypes rateType,
        String overpaymentReduceWay
    ) {
        //given
        InputData inputData = TestMortgageData.someInputData()
            .withAmount(mortgageAmount)
            .withOverpaymentReduceWay(overpaymentReduceWay)
            .withRateType(rateType);
        RateAmounts rateAmounts = TestMortgageData.someRateAmounts()
            .withOverpayment(TestMortgageData.someOverpayment());
        Rate rate = TestMortgageData.someRate()
            .withMortgageResidual(TestMortgageData.someMortgageResidual()
                .withAmount(previousResidualAmount)
                .withDuration(previousResidualDuration)
            );

        MortgageResidual expected = TestMortgageData.someMortgageResidual()
            .withAmount(expectedAmount)
            .withDuration(expectedDuration);

        //when
        MortgageResidual result = residualCalculationService.calculate(rateAmounts, rate, inputData);

        //then
        Assertions.assertEquals(expected,result);




    }
//    BigDecimal expectedAmount,
//    BigDecimal expectedDuration,
//    BigDecimal mortgageAmount,
//    BigDecimal previousResidualAmount,
//    BigDecimal previousResidualDuration,
//    String overpaymentReduceWay
    public static Stream<Arguments> testResidualData() {
        return Stream.of(
            arguments(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                RateTypes.DECREASING,
                Overpayment.REDUCE_RATE
            ),
            arguments(
                BigDecimal.valueOf(131804.63),
                BigDecimal.valueOf(124),
                BigDecimal.valueOf(165879.14),
                BigDecimal.valueOf(132414.16),
                BigDecimal.valueOf(125),
                RateTypes.CONSTANT,
                Overpayment.REDUCE_RATE
            ),
            arguments(
                BigDecimal.valueOf(4102.59),
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(147842.14),
                BigDecimal.valueOf(4712.12),
                BigDecimal.valueOf(8),
                RateTypes.DECREASING,
                Overpayment.REDUCE_PERIOD
            ),
            arguments(
                BigDecimal.valueOf(4102.59),
                BigDecimal.valueOf(4),
                BigDecimal.valueOf(147842.14),
                BigDecimal.valueOf(4712.12),
                BigDecimal.valueOf(5),
                RateTypes.CONSTANT,
                Overpayment.REDUCE_PERIOD
            )
        );
    }
}