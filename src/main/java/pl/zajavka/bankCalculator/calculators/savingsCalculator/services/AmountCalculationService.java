package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

public interface AmountCalculationService {
    BigDecimal calculateSavingAmount(SavingsData savingsData, BigDecimal interestAmount, SavingsTimePoint timePointSavings);

    BigDecimal calculateSavingAmount(SavingsData savingsData, BigDecimal interestAmount, Savings previousSaving, SavingsTimePoint timePointSavings);
}
