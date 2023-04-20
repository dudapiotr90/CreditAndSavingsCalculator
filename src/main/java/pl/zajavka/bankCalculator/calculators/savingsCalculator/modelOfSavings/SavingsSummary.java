package pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
@Builder
public record SavingsSummary(
        BigDecimal earnings,
        BigDecimal savingsSum
    ) {
}
