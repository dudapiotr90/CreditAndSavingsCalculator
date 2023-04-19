package pl.zajavka.bankCalculator.creditCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.RateAmounts;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.ConstantAmountsCalculationServiceImpl;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class ConstantAmountsCalculationServiceImplTest {

    @InjectMocks
    ConstantAmountsCalculationServiceImpl constantAmountsCalculationService;

    public static Stream<Arguments> testRateAmountsData() {
        return Stream.of(
            arguments(
                BigDecimal.ZERO,
                Overpayment.REDUCE_PERIOD,
                BigDecimal.valueOf(1035.56),
                BigDecimal.valueOf(466.67),
                BigDecimal.valueOf(568.89)),
            arguments(
                BigDecimal.ZERO,
                Overpayment.REDUCE_RATE,
                BigDecimal.valueOf(1035.56),
                BigDecimal.valueOf(466.67),
                BigDecimal.valueOf(568.89)),
            arguments(
                BigDecimal.valueOf(8517),
                Overpayment.REDUCE_RATE,
                BigDecimal.valueOf(972.56),
                BigDecimal.valueOf(438.28),
                BigDecimal.valueOf(534.28)),
            arguments(
                BigDecimal.valueOf(3357),
                Overpayment.REDUCE_PERIOD,
                BigDecimal.valueOf(1035.56),
                BigDecimal.valueOf(466.67),
                BigDecimal.valueOf(568.89))
        );
    }


    @ParameterizedTest
    @MethodSource(value = "testRateAmountsData")
    @DisplayName("Calculate constant rate amounts for first rate")
    void shouldCalculateRateAmountsForFirstRateCorrectly(
        BigDecimal overpaymentAmount,
        String overpaymentReduceWay,
        BigDecimal rateAmount,
        BigDecimal interestAmount,
        BigDecimal capitalAmount
    ) {
        //given
        MortgageData mortgageData = TestMortgageData.someMortgageData().withOverpaymentReduceWay(overpaymentReduceWay);

        Overpayment overpayment = TestMortgageData.someOverpayment().withAmount(overpaymentAmount);
        RateAmounts expected = TestMortgageData.someRateAmounts()
            .withOverpayment(overpayment)
            .withRateAmount(rateAmount)
            .withInterest(interestAmount)
            .withCapitalAmount(capitalAmount);

        //when
        RateAmounts result = constantAmountsCalculationService.calculate(mortgageData,overpayment);

        //then
        Assertions.assertEquals(expected,result);

    }

    @Test
    @DisplayName("Calculate constant rate amounts for other rates")
    void shouldCalculateRateAmountsForOtherRatesCorrectly() {
        //given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        Rate rate = TestMortgageData.someRate();
        RateAmounts expected = TestMortgageData.someRateAmounts()
            .withRateAmount(BigDecimal.valueOf(1520.84))
            .withInterest(BigDecimal.valueOf(337.46))
            .withCapitalAmount(BigDecimal.valueOf(1183.38));

        //when
        RateAmounts result = constantAmountsCalculationService.calculate(mortgageData, null, rate);



        //then
        Assertions.assertEquals(expected, result);
    }
}