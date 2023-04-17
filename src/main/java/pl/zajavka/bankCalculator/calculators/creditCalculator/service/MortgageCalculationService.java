package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;

public interface MortgageCalculationService {
    void calculate(MortgageData mortgageData);
}
