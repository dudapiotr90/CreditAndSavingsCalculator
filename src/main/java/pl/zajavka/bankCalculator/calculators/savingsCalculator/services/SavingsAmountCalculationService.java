package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;

import java.math.BigDecimal;

public interface SavingsAmountCalculationService {
    Savings calculateSaving(SavingsData savingsData, BigDecimal savingsNumber, MortgageData mortgageData);

    Savings calculateSaving(
        Savings previousSaving, BigDecimal savingsNumber, MortgageData mortgageData, SavingsData savingsData);
}
