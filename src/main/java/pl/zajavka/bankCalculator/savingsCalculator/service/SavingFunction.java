package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;

import java.math.BigDecimal;

public interface SavingFunction {
    BigDecimal calculate(Savings savings);
}
