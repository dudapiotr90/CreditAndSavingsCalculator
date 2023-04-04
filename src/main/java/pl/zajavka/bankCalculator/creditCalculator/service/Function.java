package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;

import java.math.BigDecimal;
@FunctionalInterface
public interface Function {
    BigDecimal calculate(Rate rate);
}
