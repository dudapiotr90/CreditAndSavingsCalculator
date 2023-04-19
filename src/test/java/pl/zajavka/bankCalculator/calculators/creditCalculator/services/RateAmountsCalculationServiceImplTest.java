package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RateAmountsCalculationServiceImplTest {

    @InjectMocks
    RateAmountsCalculationServiceImpl rateAmountsCalculationService;

    @Mock
    private ConstantAmountsCalculationService constantAmountsCalculationService;
    @Mock
    private DecreasingAmountsCalculationService decreasingAmountsCalculationService;


    @Test
    @DisplayName("Calculate initial rate amounts")
    void shouldCalculateInitialRateAmountsCorrectly() {
        // given
        MortgageData mortgageData1 = TestMortgageData.someMortgageData();
        MortgageData mortgageData2 = TestMortgageData.someMortgageData().withRateType(RateTypes.DECREASING);
        Overpayment overpayment = TestMortgageData.someOverpayment();

        RateAmounts expected = TestMortgageData.someRateAmounts();

        Mockito.when(constantAmountsCalculationService.calculate(any(), any())).thenReturn(expected);
        Mockito.when(decreasingAmountsCalculationService.calculate(any(), any())).thenReturn(expected);

        // when
        RateAmounts result1 = rateAmountsCalculationService.calculate(mortgageData1, overpayment);
        RateAmounts result2 = rateAmountsCalculationService.calculate(mortgageData2, overpayment);

        // then
        Assertions.assertEquals(expected, result1);
        Assertions.assertEquals(expected, result2);

    }


    @Test
    @DisplayName("Calculate other rate amounts")
    void shouldCalculateOtherRateAmountsCorrectly() {
        // given
        MortgageData mortgageData1 = TestMortgageData.someMortgageData();
        MortgageData mortgageData2 = TestMortgageData.someMortgageData().withRateType(RateTypes.DECREASING);
        Overpayment overpayment = TestMortgageData.someOverpayment();
        Rate rate = TestMortgageData.someRate();

        RateAmounts expected = TestMortgageData.someRateAmounts();

        Mockito.when(constantAmountsCalculationService.calculate(any(), any(),any())).thenReturn(expected);
        Mockito.when(decreasingAmountsCalculationService.calculate(any(), any(),any())).thenReturn(expected);

        // when
        RateAmounts result1 = rateAmountsCalculationService.calculate(mortgageData1, overpayment,rate);
        RateAmounts result2 = rateAmountsCalculationService.calculate(mortgageData2, overpayment,rate);

        // then
        Assertions.assertEquals(expected, result1);
        Assertions.assertEquals(expected, result2);

    }
}