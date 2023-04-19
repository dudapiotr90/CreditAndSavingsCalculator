package pl.zajavka.bankCalculator.creditCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.ResidualCalculationServiceImpl;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
@ExtendWith(MockitoExtension.class)
class ResidualCalculationServiceImplTest {

    @InjectMocks
    ResidualCalculationServiceImpl residualCalculationService;


    @Test
    void shouldCalculateResidualForFirstRateCorrectly() {
        //given
        MortgageData mortgageData1 = TestMortgageData.someMortgageData();
        MortgageData mortgageData2 = TestMortgageData.someMortgageData()
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
        MortgageResidual result1 = residualCalculationService.calculate(rateAmounts, mortgageData1);
        MortgageResidual result2 = residualCalculationService.calculate(rateAmounts, mortgageData2);

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
        MortgageData mortgageData = TestMortgageData.someMortgageData()
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
        MortgageResidual result = residualCalculationService.calculate(rateAmounts, rate, mortgageData);

        //then
        Assertions.assertEquals(expected,result);




    }
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