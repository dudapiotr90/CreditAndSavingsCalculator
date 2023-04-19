package pl.zajavka.bankCalculator.fixtures;

import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

public class TestMortgageData {
    public static MortgageData someInputData() {
        return MortgageData.builder()
            .repaymentStartDate(LocalDate.of(2023, 1, 1))
            .monthsDuration(BigDecimal.valueOf(180))
            .amount(BigDecimal.valueOf(140000))
            .rateType(RateTypes.CONSTANT)
            .wiborPercentage(BigDecimal.valueOf(2))
            .bankMarginPercent(BigDecimal.valueOf(1.5))
            .overpaymentReduceWay(Overpayment.REDUCE_RATE)
            .overpaymentProvisionPercent(BigDecimal.valueOf(3.54))
            .overpaymentProvisionMonths(BigDecimal.valueOf(12))
            .overpaymentSchema(Collections.emptyMap())
            .build();
    }

    public static MortgageTimePoint someTimePoint() {
        return MortgageTimePoint.builder()
            .date(LocalDate.of(2023, 1, 1))
            .year(BigDecimal.ONE)
            .month(BigDecimal.ONE)
            .build();

    }

    public static Rate someRate() {
        return Rate.builder()
            .mortgageTimePoint(someTimePoint())
            .mortgageResidual(someMortgageResidual())
            .mortgageReference(someMortgageReference())
            .build();
    }

    public static MortgageResidual someMortgageResidual() {
        return MortgageResidual.builder()
            .amount(BigDecimal.valueOf(101238.21))
            .duration(BigDecimal.valueOf(60))
            .build();
    }

    public static MortgageReference someMortgageReference() {
        return MortgageReference.builder()
            .amount(BigDecimal.valueOf(150213.67))
            .duration(BigDecimal.valueOf(120))
            .build();
    }

    public static RateAmounts someRateAmounts() {
        return RateAmounts.builder()
            .rateAmount(BigDecimal.valueOf(1109.53))
            .interest(BigDecimal.valueOf(500.00))
            .capitalAmount(BigDecimal.valueOf(609.53))
            .build();
    }

    public static Overpayment someOverpayment() {
        return Overpayment.builder()
            .amount(BigDecimal.ZERO)
            .provisionAmount(BigDecimal.ZERO)
            .build();
    }

}
