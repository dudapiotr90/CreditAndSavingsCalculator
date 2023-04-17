package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;

import java.math.BigDecimal;
@FunctionalInterface
public interface Function {
    BigDecimal calculate(Rate rate);
}
