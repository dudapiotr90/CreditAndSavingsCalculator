package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.InterestCapitalization;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;
import pl.zajavka.bankCalculator.fixtures.TestSavingsData;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavingsAmountCalculationServiceImplTest {

    @InjectMocks
    SavingsAmountCalculationServiceImpl savingsAmountCalculationService;

    @Mock
    private SavingsTimeCalculationService savingsTimeCalculationService;
    @Mock
    private InterestCalculationService interestCalculationService;
    @Mock
    private AmountCalculationService amountCalculationService;

    @Test
    @DisplayName("Calculate Initial Saving with Monthly Capitalization")
    void shouldCalculateInitialSavingAfterMonthCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        SavingsData savingsData = TestSavingsData.someSavingsData()
            .withInterestCapitalization(InterestCapitalization.AFTER_MONTH);
        SavingsTimePoint timePoint = TestSavingsData.someSavingsTimePoint();

        Savings expected = TestSavingsData.someSavings()
            .withSavingsNumber(BigDecimal.ONE);

        when(savingsTimeCalculationService.calculate(any(SavingsData.class), any(BigDecimal.class),
            any(MortgageData.class))).thenReturn(timePoint);

        when(interestCalculationService.calculateInterestByMonth(any(SavingsData.class), any(SavingsTimePoint.class)))
            .thenReturn(BigDecimal.valueOf(975.77));

        when(amountCalculationService.calculateSavingAmount(any(SavingsData.class), any(BigDecimal.class),
            any(SavingsTimePoint.class))).thenReturn(BigDecimal.valueOf(14957.12));

        // when
        Savings result = savingsAmountCalculationService.calculateSaving(savingsData, BigDecimal.ONE, mortgageData);

        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Calculate Initial Saving with Yearly Capitalization")
    void shouldCalculateInitialSavingAfterYearCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        SavingsData savingsData = TestSavingsData.someSavingsData()
            .withInterestCapitalization(InterestCapitalization.AFTER_YEAR);
        SavingsTimePoint timePoint = TestSavingsData.someSavingsTimePoint();

        Savings expected = TestSavingsData.someSavings()
            .withSavingsNumber(BigDecimal.ONE);

        when(savingsTimeCalculationService.calculate(any(SavingsData.class), any(BigDecimal.class),
            any(MortgageData.class))).thenReturn(timePoint);

        when(interestCalculationService.calculateInterestByYear(any(SavingsData.class), any(SavingsTimePoint.class)))
            .thenReturn(BigDecimal.valueOf(975.77));

        when(amountCalculationService.calculateSavingAmount(any(SavingsData.class), any(BigDecimal.class),
            any(SavingsTimePoint.class))).thenReturn(BigDecimal.valueOf(14957.12));

        // when
        Savings result = savingsAmountCalculationService.calculateSaving(savingsData, BigDecimal.ONE, mortgageData);
        // then
        Assertions.assertEquals(expected, result);
    }


    @Test
    @DisplayName("Calculate Initial Saving with Quarterly Capitalization")
    void shouldCalculateInitialSavingAfterQuarterCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        SavingsData savingsData = TestSavingsData.someSavingsData()
            .withInterestCapitalization(InterestCapitalization.AFTER_QUARTER);
        SavingsTimePoint timePoint = TestSavingsData.someSavingsTimePoint();

        Savings expected = TestSavingsData.someSavings()
            .withSavingsNumber(BigDecimal.ONE);

        when(savingsTimeCalculationService.calculate(any(SavingsData.class), any(BigDecimal.class),
            any(MortgageData.class))).thenReturn(timePoint);

        when(interestCalculationService.calculateInterestByQuarter(any(SavingsData.class), any(SavingsTimePoint.class)))
            .thenReturn(BigDecimal.valueOf(975.77));

        when(amountCalculationService.calculateSavingAmount(any(SavingsData.class), any(BigDecimal.class),
            any(SavingsTimePoint.class))).thenReturn(BigDecimal.valueOf(14957.12));

        // when
        Savings result = savingsAmountCalculationService.calculateSaving(savingsData, BigDecimal.ONE, mortgageData);
        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Calculate Other Saving with Monthly Capitalization")
    void shouldCalculateOtherSavingAfterMonthCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        SavingsData savingsData = TestSavingsData.someSavingsData()
            .withInterestCapitalization(InterestCapitalization.AFTER_MONTH);
        SavingsTimePoint timePoint = TestSavingsData.someSavingsTimePoint();

        Savings expected = TestSavingsData.someSavings();


        when(savingsTimeCalculationService.calculate(any(SavingsData.class), any(BigDecimal.class),
            any(Savings.class))).thenReturn(timePoint);

        when(interestCalculationService.calculateInterestByMonth(any(SavingsData.class),
            any(Savings.class), any(SavingsTimePoint.class)))
            .thenReturn(BigDecimal.valueOf(975.77));

        when(amountCalculationService.calculateSavingAmount(any(SavingsData.class), any(BigDecimal.class),
            any(Savings.class), any(SavingsTimePoint.class))).thenReturn(BigDecimal.valueOf(14957.12));

        // when
        Savings result = savingsAmountCalculationService.calculateSaving(expected,BigDecimal.valueOf(13),mortgageData,
            savingsData);

        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Calculate Other Saving with Yearly Capitalization")
    void shouldCalculateOtherSavingAfterYearCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        SavingsData savingsData = TestSavingsData.someSavingsData()
            .withInterestCapitalization(InterestCapitalization.AFTER_YEAR);
        SavingsTimePoint timePoint = TestSavingsData.someSavingsTimePoint();

        Savings expected = TestSavingsData.someSavings();

        when(savingsTimeCalculationService.calculate(any(SavingsData.class), any(BigDecimal.class),
            any(Savings.class))).thenReturn(timePoint);

        when(interestCalculationService.calculateInterestByYear(any(SavingsData.class),
            any(Savings.class), any(SavingsTimePoint.class)))
            .thenReturn(BigDecimal.valueOf(975.77));

        when(amountCalculationService.calculateSavingAmount(any(SavingsData.class), any(BigDecimal.class),
            any(Savings.class), any(SavingsTimePoint.class))).thenReturn(BigDecimal.valueOf(14957.12));

        // when
        Savings result = savingsAmountCalculationService.calculateSaving(expected,BigDecimal.valueOf(13),mortgageData,
            savingsData);
        // then
        Assertions.assertEquals(expected, result);
    }


    @Test
    @DisplayName("Calculate Other Saving with Quarterly Capitalization")
    void shouldCalculateOtherSavingAfterQuarterCorrectly() {
        // given
        MortgageData mortgageData = TestMortgageData.someMortgageData();
        SavingsData savingsData = TestSavingsData.someSavingsData()
            .withInterestCapitalization(InterestCapitalization.AFTER_QUARTER);
        SavingsTimePoint timePoint = TestSavingsData.someSavingsTimePoint();

        Savings expected = TestSavingsData.someSavings();

        when(savingsTimeCalculationService.calculate(any(SavingsData.class), any(BigDecimal.class),
            any(Savings.class))).thenReturn(timePoint);

        when(interestCalculationService.calculateInterestByQuarter(any(SavingsData.class),
            any(Savings.class), any(SavingsTimePoint.class)))
            .thenReturn(BigDecimal.valueOf(975.77));

        when(amountCalculationService.calculateSavingAmount(any(SavingsData.class), any(BigDecimal.class),
            any(Savings.class), any(SavingsTimePoint.class))).thenReturn(BigDecimal.valueOf(14957.12));

        // when
        Savings result = savingsAmountCalculationService.calculateSaving(expected,BigDecimal.valueOf(13),mortgageData,
            savingsData);
        // then
        Assertions.assertEquals(expected, result);
    }

    @Test()
    void shouldThrowExceptionWhenCalculatingInitialSaving() {
        // given
        SavingsData savingsData =TestSavingsData.someSavingsData();
        MortgageData mortgageData = TestMortgageData.someMortgageData();

        String expectedExceptionMessage = "This method only accepts savingsNumber equal to ONE";

        // when
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () ->
            savingsAmountCalculationService.calculateSaving(savingsData, BigDecimal.TEN, mortgageData));

        // then
        Assertions.assertEquals(expectedExceptionMessage,exception.getMessage());
    }
}