package pl.zajavka.bankCalculator.creditCalculator.modelOfCredit;

import java.math.BigDecimal;

public record MortgageSummary(
    BigDecimal interestSum,
    BigDecimal overpaymentProvisions,
    BigDecimal totalLosses,
    BigDecimal totalCapital
){

}
