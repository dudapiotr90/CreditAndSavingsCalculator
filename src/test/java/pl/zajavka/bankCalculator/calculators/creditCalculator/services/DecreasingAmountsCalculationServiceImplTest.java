package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageResidual;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.RateAmounts;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

import java.math.BigDecimal;
@ExtendWith(MockitoExtension.class)
class DecreasingAmountsCalculationServiceImplTest {

    @InjectMocks
    DecreasingAmountsCalculationServiceImpl decreasingAmountsCalculationService;


    @Test
    @DisplayName("Calculate decreasing rate amounts for first rate")
    void shouldCalculateRateAmountsForFirstRateCorrectly() {
        //given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        RateAmounts expected = TestMortgageData.someRateAmounts()
            .withRateAmount(BigDecimal.valueOf(1244.45))
            .withInterest(BigDecimal.valueOf(466.67))
            .withCapitalAmount(BigDecimal.valueOf(777.78));
        //when
        RateAmounts result = decreasingAmountsCalculationService.calculate(mortgageData, null);
        //then
        Assertions.assertEquals(expected,result);
    }

    @Test
    @DisplayName("Calculate decreasing rate amounts for other rates")
    void shouldCalculateRateAmountsForOtherRatesCorrectly() {
        //given
        MortgageData mortgageData = TestMortgageData.someMortgageData();

        Rate rate1 = TestMortgageData.someRate();
        RateAmounts expected1 = TestMortgageData.someRateAmounts()
            .withRateAmount(BigDecimal.valueOf(1589.24))
            .withInterest(BigDecimal.valueOf(337.46))
            .withCapitalAmount(BigDecimal.valueOf(1251.78));

        Rate rate2 = TestMortgageData.someRate()
            .withMortgageResidual(
                new MortgageResidual(BigDecimal.valueOf(1251.99), BigDecimal.ONE));
        RateAmounts expected2 = TestMortgageData.someRateAmounts()
            .withRateAmount(BigDecimal.valueOf(1256.16))
            .withInterest(BigDecimal.valueOf(4.17))
            .withCapitalAmount(BigDecimal.valueOf(1251.99));


        //when
        RateAmounts result1 = decreasingAmountsCalculationService.calculate(mortgageData, null,rate1);
        RateAmounts result2 = decreasingAmountsCalculationService.calculate(mortgageData, null,rate2);
        //then
        Assertions.assertEquals(expected1,result1);
        Assertions.assertEquals(expected2,result2);

    }
}