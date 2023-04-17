package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageTimePoint;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;

import java.math.BigDecimal;

public interface TimePointCalculationService {

    MortgageTimePoint calculate(BigDecimal rateNumberIndex, MortgageData mortgageData);

    MortgageTimePoint calculate(BigDecimal rateNumber, Rate previousRate);
}
