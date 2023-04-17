package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;

import java.util.List;

public interface MortgageSummaryService {

    MortgageSummary calculate(List<Rate> rates);

}
