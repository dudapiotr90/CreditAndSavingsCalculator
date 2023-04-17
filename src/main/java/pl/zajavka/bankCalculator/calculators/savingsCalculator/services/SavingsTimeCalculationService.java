package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

public interface SavingsTimeCalculationService {
    SavingsTimePoint calculate(SavingsData savingsData, BigDecimal savingsNumber, MortgageData mortgageData);

    SavingsTimePoint calculate(SavingsData savingsData, BigDecimal savingsNumber,  Savings previousSaving);
}
