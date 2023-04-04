package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;

import java.math.BigDecimal;

public interface OverpaymentCalculationService {
    Overpayment calculate(BigDecimal rateNumber, InputData inputData);

}
