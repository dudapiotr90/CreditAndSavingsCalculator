package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageTimePoint;

import java.math.BigDecimal;

public interface TimePointCalculationService {

    MortgageTimePoint calculate(BigDecimal rateNumberIndex, InputData inputData);

    MortgageTimePoint calculate(BigDecimal rateNumber, Rate previousRate);
}
