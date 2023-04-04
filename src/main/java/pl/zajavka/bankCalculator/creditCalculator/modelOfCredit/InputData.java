package pl.zajavka.bankCalculator.creditCalculator.modelOfCredit;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.PERCENT;

@With
@Builder
public record InputData(
    LocalDate repaymentStartDate,
    BigDecimal monthsDuration,
    BigDecimal amount,
    RateTypes rateType,
    BigDecimal wiborPercentage,
    BigDecimal bankMarginPercent,
    String overpaymentReduceWay,
    BigDecimal overpaymentProvisionPercent,
    BigDecimal overpaymentProvisionMonths,
    Map<Integer, BigDecimal> overpaymentSchema,
    boolean mortgagePrintPayoffsSchedule,
    Integer mortgageRateNumberToPrint,

    boolean compareToSavings
) {

    public BigDecimal interestPercent() {
        return bankMarginPercent().add(wiborPercentage()).divide(PERCENT, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal interestDisplay() {
        return wiborPercentage.add(bankMarginPercent).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal overpaymentProvisionPercent() {
        return overpaymentProvisionPercent.divide(PERCENT, 2, RoundingMode.HALF_UP);
    }
}
