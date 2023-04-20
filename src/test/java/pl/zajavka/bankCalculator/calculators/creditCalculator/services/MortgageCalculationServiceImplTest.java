package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.commonServices.PrintingService;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MortgageCalculationServiceImplTest {

    @InjectMocks
    MortgageCalculationServiceImpl mortgageCalculationService;

    @Mock
    private PrintingService printingService;
    @Mock
    private RateCalculationService rateCalculationService;
    @Mock
    private MortgageSummaryService mortgageSummaryService;

    @Test
    void shouldCallInsideMethodsCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        MortgageSummary mortgageSummary = TestMortgageData.someMortgageSummary();
        List<Rate> expectedRates = TestMortgageData.someRates();

        when(mortgageSummaryService.calculate(any()))
            .thenReturn(mortgageSummary.withOverpaymentProvisions(BigDecimal.valueOf(1354)))
            .thenReturn(mortgageSummary);
        when(rateCalculationService.calculate(any(MortgageData.class)))
            .thenReturn(expectedRates);

        // when
        mortgageCalculationService.calculate(mortgageData);

        // then
        verify(printingService).printIntroInformation(any(MortgageData.class));
        verify(printingService).printOverpaymentProfit(any(MortgageSummary.class),any(MortgageSummary.class)            );
        verify(printingService).printMortgageSummary(any(MortgageSummary.class));

        verify(printingService).printRatesSchedule(anyList(), any(MortgageData.class));

        verify(rateCalculationService, times(2)).calculate(any(MortgageData.class));
        verify(mortgageSummaryService, times(2)).calculate(anyList());

    }

}

