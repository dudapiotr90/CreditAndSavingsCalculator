package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.RateAmounts;

public interface RateAmountsCalculationService {
    RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment);

    RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment, Rate previousRate);
}
