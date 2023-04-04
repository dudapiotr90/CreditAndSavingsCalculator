package pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InterestCapitalization {
    AFTER_YEAR("ROCZNA"),
    AFTER_MONTH("MIESIĘCZNA"),
    AFTER_QUARTER("KWARTALNA");

    @Getter
    private final String value;
}
