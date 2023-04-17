package pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit;

import java.math.BigDecimal;

public record MortgageSummary(
    BigDecimal interestSum,
    BigDecimal overpaymentProvisions,
    BigDecimal totalLosses,
    BigDecimal totalCapital
){}
