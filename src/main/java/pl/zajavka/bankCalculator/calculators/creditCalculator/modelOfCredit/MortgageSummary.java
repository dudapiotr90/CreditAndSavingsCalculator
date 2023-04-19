package pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
@Builder
public record MortgageSummary(
    BigDecimal interestSum,
    BigDecimal overpaymentProvisions,
    BigDecimal totalLosses,
    BigDecimal totalCapital
){}
