package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;

import java.math.BigDecimal;

public interface SavingFunction {
    BigDecimal calculate(Savings savings);
}
