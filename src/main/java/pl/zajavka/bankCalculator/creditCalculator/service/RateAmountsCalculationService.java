package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateAmounts;

public interface RateAmountsCalculationService {
    RateAmounts calculate(InputData inputData, Overpayment overpayment);

    RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate);
}
