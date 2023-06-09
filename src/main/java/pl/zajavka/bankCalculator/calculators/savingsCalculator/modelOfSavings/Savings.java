package pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
@Builder
public record Savings(
    SavingsAmount savingsAmount,
    SavingsTimePoint savingsTimePoint,

    BigDecimal savingsNumber) {
}
