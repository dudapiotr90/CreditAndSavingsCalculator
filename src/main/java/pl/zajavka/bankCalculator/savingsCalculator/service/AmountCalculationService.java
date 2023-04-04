package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

public interface AmountCalculationService {
    BigDecimal calculateSavingAmount(SavingsData savingsData, BigDecimal interestAmount, SavingsTimePoint timePointSavings);

    BigDecimal calculateSavingAmount(SavingsData savingsData, BigDecimal interestAmount, Savings previousSaving, SavingsTimePoint timePointSavings);
}
