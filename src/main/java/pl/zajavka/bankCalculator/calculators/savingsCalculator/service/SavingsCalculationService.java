package pl.zajavka.bankCalculator.calculators.savingsCalculator.service;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;

public interface SavingsCalculationService {

    void calculate(SavingsData savingsData, MortgageData mortgageData);
}
