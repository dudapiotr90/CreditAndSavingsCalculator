package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;

import java.math.BigDecimal;

public interface SavingsAmountCalculationService {
    Savings calculateSaving(SavingsData savingsData, BigDecimal savingsNumber, InputData inputData);

    Savings calculateSaving(
        Savings previousSaving, BigDecimal savingsNumber, InputData inputData, SavingsData savingsData);
}
