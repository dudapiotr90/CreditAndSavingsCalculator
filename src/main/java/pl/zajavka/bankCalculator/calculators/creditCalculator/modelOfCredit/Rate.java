package pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
@Builder
public record Rate(
    BigDecimal rateNumber,
    MortgageTimePoint mortgageTimePoint,
    RateAmounts rateAmounts,
    MortgageResidual mortgageResidual,
    MortgageReference mortgageReference) {

}
