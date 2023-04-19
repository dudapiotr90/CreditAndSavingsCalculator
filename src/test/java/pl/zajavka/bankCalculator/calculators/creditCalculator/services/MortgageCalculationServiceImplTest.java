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
    void shouldPrintMortgageInformationCorrectly() {
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
        verify(printingService).printIntroInformation(mortgageData);
        verify(printingService).printOverpaymentProfit(TestMortgageData.someMortgageSummary(),
            TestMortgageData.someMortgageSummary().withOverpaymentProvisions(new BigDecimal("1354")));
        verify(printingService).printMortgageSummary(
            TestMortgageData.someMortgageSummary().withOverpaymentProvisions(BigDecimal.valueOf(1354)));
        verify(printingService).printRatesSchedule(expectedRates,mortgageData);

        verify(rateCalculationService, times(2)).calculate(mortgageData);
        verify(mortgageSummaryService, times(2)).calculate(expectedRates);

    }

}

