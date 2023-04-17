package pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings;

import java.math.BigDecimal;

public record SavingsSummary(
        BigDecimal earnings,
        BigDecimal savingsSum
    ) {
}
