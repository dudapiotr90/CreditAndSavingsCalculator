package pl.zajavka.bankCalculator.calculators.savingsCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.services.AmountCalculationServiceImpl;
import pl.zajavka.bankCalculator.fixtures.TestSavingsData;

import java.math.BigDecimal;
@ExtendWith(MockitoExtension.class)
class AmountCalculationServiceImplTest {
@InjectMocks
    AmountCalculationServiceImpl amountCalculationService;

    @Test
    void shouldCalculateSavingAmountCorrectlyForFirstDeposit() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        BigDecimal interestAmount = BigDecimal.valueOf(822.12);
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();

        BigDecimal expected = BigDecimal.valueOf(8980.69);
        //when
        BigDecimal result = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);

    }

    @Test
    void shouldCalculateSavingAmountCorrectlyForOtherDeposits() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        BigDecimal interestAmount = BigDecimal.valueOf(962.53);
        SavingsTimePoint savingsTimePoint = TestSavingsData.someSavingsTimePoint();
        Savings savings = TestSavingsData.someSavings();

        BigDecimal expected = BigDecimal.valueOf(24078.22);

        //when
        BigDecimal result = amountCalculationService.calculateSavingAmount(
        savingsData, interestAmount, savings, savingsTimePoint);

        //then
        Assertions.assertEquals(expected,result);

    }
}