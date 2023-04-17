package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;

import java.util.List;

public interface RateCalculationService {
    List<Rate> calculate(final MortgageData mortgageData);

}
