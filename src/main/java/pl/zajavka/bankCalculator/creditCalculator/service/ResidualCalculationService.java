package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.*;

public interface ResidualCalculationService {
    MortgageResidual calculate(RateAmounts rateAmounts, InputData inputData);

    MortgageResidual calculate(RateAmounts rateAmounts, Rate previousRate, InputData inputData);
}
