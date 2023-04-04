package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;

import java.util.List;

public interface RateCalculationService {
    List<Rate> calculate(final InputData inputData);

}
