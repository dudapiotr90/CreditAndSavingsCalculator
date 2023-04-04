package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsSummary;

import java.util.List;

public interface SavingsSummaryService {
    SavingsSummary calculateSavings(List<Savings> savings);
}
