package pl.zajavka.bankCalculator.savingsCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.services.InterestCalculationService;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.services.InterestCalculationServiceImpl;

import java.math.BigDecimal;

class InterestCalculationServiceImplTest {

    InterestCalculationService interestCalculationService;

    @BeforeEach
    void setUp() {
        this.interestCalculationService = new InterestCalculationServiceImpl();
    }

    @Test
    void shouldCalculateInterestByYearForFirstDepositCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();

        BigDecimal expected = BigDecimal.valueOf(318.18);

        //when
        BigDecimal result = interestCalculationService.calculateInterestByYear(savingsData, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);

    }

    @Test
    void shouldCalculateInterestByYearForOtherDepositsCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();
        Savings savings = TestSavingsData.someSavings();

        BigDecimal expected = BigDecimal.valueOf(901.51);

        //when
        BigDecimal result = interestCalculationService.calculateInterestByYear(savingsData, savings, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);
    }

    @Test
    void shouldCalculateInterestByQuarterForFirstDepositCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();

        BigDecimal expected = BigDecimal.valueOf(181.21);

        //when
        BigDecimal result = interestCalculationService.calculateInterestByQuarter(savingsData, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);

    }

    @Test
    void shouldCalculateInterestByQuarterForOtherDepositsCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();
        Savings savings = TestSavingsData.someSavings();

        BigDecimal expected = BigDecimal.valueOf(591.91);

        //when
        BigDecimal result = interestCalculationService.calculateInterestByQuarter(savingsData, savings, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);
    }

    @Test
    void shouldCalculateInterestByMonthForFirstDepositCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();

        BigDecimal expected = BigDecimal.valueOf(193.44);

        //when
        BigDecimal result = interestCalculationService.calculateInterestByMonth(savingsData, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);

    }

    @Test
    void shouldCalculateInterestByMonthForOtherDepositsCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();
        Savings savings = TestSavingsData.someSavings();

        BigDecimal expected = BigDecimal.valueOf(787.31);

        //when
        BigDecimal result = interestCalculationService.calculateInterestByMonth(savingsData, savings, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);
    }
}