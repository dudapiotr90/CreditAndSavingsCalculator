package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;

public interface SavingsCalculationService {

    void calculate(SavingsData savingsData, InputData inputData);
}
