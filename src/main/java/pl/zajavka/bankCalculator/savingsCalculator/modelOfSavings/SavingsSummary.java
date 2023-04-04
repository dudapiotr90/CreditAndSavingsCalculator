package pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings;

import java.math.BigDecimal;

public record SavingsSummary(
        BigDecimal earnings,
        BigDecimal savingsSum
    ) {
}
