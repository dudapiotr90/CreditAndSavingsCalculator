package pl.zajavka.bankCalculator.creditCalculator.modelOfCredit;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
@Builder
public record RateAmounts(
    BigDecimal rateAmount,
    BigDecimal interest,
    BigDecimal capitalAmount,
    Overpayment overpayment) {

}

