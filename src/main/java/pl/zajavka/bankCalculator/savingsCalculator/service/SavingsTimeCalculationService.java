package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

public interface SavingsTimeCalculationService {
    SavingsTimePoint calculate(SavingsData savingsData, BigDecimal savingsNumber, InputData inputData);

    SavingsTimePoint calculate(SavingsData savingsData, BigDecimal savingsNumber,  Savings previousSaving);
}
