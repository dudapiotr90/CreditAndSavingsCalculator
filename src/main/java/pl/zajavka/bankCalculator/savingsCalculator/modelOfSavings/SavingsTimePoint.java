package pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@With
@Builder
public record SavingsTimePoint(LocalDate date, BigDecimal year, List<Integer> months) {

}

