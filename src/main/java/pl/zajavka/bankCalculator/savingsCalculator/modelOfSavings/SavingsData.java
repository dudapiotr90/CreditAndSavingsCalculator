package pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings;

import lombok.Builder;
import lombok.With;
import pl.zajavka.bankCalculator.globalServices.CalculatingMethods;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

//TODO depositDuration for NON_RENAVABLE saving type
@With
@Builder
public record SavingsData(
    BigDecimal depositDuration,
    Map<Integer,BigDecimal> depositSchema,

    SavingsType savingType,
    BigDecimal depositInterest,
    InterestCapitalization interestCapitalization,
    BigDecimal tax

) {
    @Override
    public BigDecimal tax() {
        return tax.divide(CalculatingMethods.PERCENT, 2, RoundingMode.HALF_UP);
    }
    @Override
    public BigDecimal depositInterest() {
        return depositInterest.divide(CalculatingMethods.PERCENT, 2, RoundingMode.HALF_UP);
    }
    public BigDecimal depositInterestDisplay() {
        return depositInterest;
    }
}
