package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;

import java.math.BigDecimal;

public interface OverpaymentCalculationService {
    Overpayment calculate(BigDecimal rateNumber, MortgageData mortgageData);

}
