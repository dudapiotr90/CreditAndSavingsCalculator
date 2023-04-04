package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.*;

public interface ReferenceCalculationService {
    MortgageReference calculate(InputData inputData, Overpayment overpayment);

    MortgageReference calculate(InputData inputData, RateAmounts rateAmounts, Rate previousRate);
}
