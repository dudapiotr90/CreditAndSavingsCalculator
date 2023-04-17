package pl.zajavka.bankCalculator.calculators.savingsCalculator.service;

import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

public interface InterestCalculationService {

    BigDecimal calculateInterestByYear(SavingsData savingsData, SavingsTimePoint timePointSavings);

    BigDecimal calculateInterestByQuarter(SavingsData savingsData, SavingsTimePoint timePointSavings);

    BigDecimal calculateInterestByMonth(SavingsData savingsData, SavingsTimePoint timePointSavings);


    BigDecimal calculateInterestByYear(SavingsData savingsData, Savings previousSaving, SavingsTimePoint timePointSavings);

    BigDecimal calculateInterestByQuarter(SavingsData savingsData, Savings previousSaving, SavingsTimePoint timePointSavings);

    BigDecimal calculateInterestByMonth(SavingsData savingsData, Savings previousSaving, SavingsTimePoint timePointSavings);

}
