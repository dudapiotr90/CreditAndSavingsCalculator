package pl.zajavka.bankCalculator.creditCalculator.modelOfCredit;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
@With
@Builder
public record MortgageTimePoint(LocalDate date, BigDecimal year, BigDecimal month) {
}
