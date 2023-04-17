package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsSummary;

import java.util.List;

public interface SavingsSummaryService {
    SavingsSummary calculateSavings(List<Savings> savings);
}
