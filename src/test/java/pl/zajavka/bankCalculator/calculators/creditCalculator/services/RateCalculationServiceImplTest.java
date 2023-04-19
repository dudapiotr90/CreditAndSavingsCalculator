package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.RateAmounts;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RateCalculationServiceImplTest {

    @InjectMocks
    RateCalculationServiceImpl rateCalculationService;

    @Mock
    private TimePointCalculationService timePointCalculationService;
    @Mock
    private ResidualCalculationService residualCalculationService;
    @Mock
    private OverpaymentCalculationService overpaymentCalculationService;
    @Mock
    private RateAmountsCalculationService rateAmountsCalculationService;
    @Mock
    private ReferenceCalculationService referenceCalculationService;

    @Test
    void ratesCalculatedCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        List<Rate> expected = TestMortgageData.someRates();

        when(timePointCalculationService.calculate(any(BigDecimal.class), any(MortgageData.class)))
            .thenReturn(TestMortgageData.someTimePoint());
        when(timePointCalculationService.calculate(any(BigDecimal.class), any(Rate.class)))
            .thenReturn(TestMortgageData.someTimePoint());

        when(rateAmountsCalculationService.calculate(any(), any()))
            .thenReturn(TestMortgageData.someRateAmounts());
        when(rateAmountsCalculationService.calculate(any(), any(), any()))
            .thenReturn(TestMortgageData.someRateAmounts());

        when(residualCalculationService.calculate(any(RateAmounts.class), any(MortgageData.class)))
            .thenReturn(TestMortgageData.someMortgageResidual());
        when(residualCalculationService.calculate(any(RateAmounts.class), any(Rate.class), any(MortgageData.class)))
            .thenReturn(TestMortgageData.someMortgageResidual());

        when(referenceCalculationService.calculate(any(MortgageData.class),any(Overpayment.class) ))
            .thenReturn(TestMortgageData.someMortgageReference());
        when(referenceCalculationService.calculate(any(MortgageData.class),any(RateAmounts.class),  any(Rate.class)))
            .thenReturn(TestMortgageData.someMortgageReference());

        when(overpaymentCalculationService.calculate(any(BigDecimal.class), any(MortgageData.class)))
            .thenReturn(TestMortgageData.someOverpayment());

        // when
        List<Rate> result = rateCalculationService.calculate(mortgageData);

        // then
        Assertions.assertEquals(expected,result);
    }


}