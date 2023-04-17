package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;

public interface ReferenceCalculationService {
    MortgageReference calculate(MortgageData mortgageData, Overpayment overpayment);

    MortgageReference calculate(MortgageData mortgageData, RateAmounts rateAmounts, Rate previousRate);
}
