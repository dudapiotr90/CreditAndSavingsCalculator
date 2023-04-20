package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.commonServices.Comparison;
import pl.zajavka.bankCalculator.calculators.commonServices.PrintingService;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsSummary;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;
import pl.zajavka.bankCalculator.fixtures.TestSavingsData;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingsCalculationServiceImplTest {

    @InjectMocks
    SavingsCalculationServiceImpl savingsCalculationService;

    @Mock
    private PrintingService printingService;
    @Mock
    private SavingsAmountCalculationService savingsAmountCalculationService;
    @Mock
    private SavingsSummaryService savingsSummaryService;

    @Test
    void shouldCallInsideMethodsCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        SavingsData savingsData = TestSavingsData.someSavingsData();
        SavingsSummary savingsSummary = TestSavingsData.someSavingsSummary();
        Savings savings = TestSavingsData.someSavings();

        Comparison comparisonMock = mock(Comparison.class);
        try (MockedStatic<Comparison> comparisonMockedStatic = mockStatic(Comparison.class)) {

            when(savingsSummaryService.calculateSavings(anyList())).thenReturn(savingsSummary);
            when(savingsAmountCalculationService.calculateSaving(any(SavingsData.class), any(BigDecimal.class),
                any(MortgageData.class))).thenReturn(savings);
            when(savingsAmountCalculationService.calculateSaving(any(Savings.class), any(BigDecimal.class),
                any(MortgageData.class), any(SavingsData.class))).thenReturn(savings);
            when(Comparison.getInstance()).thenReturn(comparisonMock);

            // when
            savingsCalculationService.calculate(savingsData, mortgageData);
        }

        // then
        verify(savingsSummaryService).calculateSavings(anyList());
        verify(savingsAmountCalculationService).calculateSaving(any(SavingsData.class), any(BigDecimal.class),
            any(MortgageData.class));
        verify(savingsAmountCalculationService, times(14)).calculateSaving(any(Savings.class), any(BigDecimal.class),
            any(MortgageData.class), any(SavingsData.class));

        verify(printingService).printSavingsInformation(any(SavingsData.class),any(MortgageData.class));
        verify(printingService).printSavingsSummary(any(SavingsSummary.class));
        verify(printingService).printSavings(anyList());
        verify(printingService).printComparison(any(),any(MortgageData.class));

    }
}