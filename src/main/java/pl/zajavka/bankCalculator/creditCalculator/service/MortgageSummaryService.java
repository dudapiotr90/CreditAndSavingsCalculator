package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageSummary;

import java.util.List;

public interface MortgageSummaryService {

    MortgageSummary calculate(List<Rate> rates);

}
