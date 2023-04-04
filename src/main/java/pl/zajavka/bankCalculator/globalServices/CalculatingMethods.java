package pl.zajavka.bankCalculator.globalServices;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateAmounts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CalculatingMethods {
    public static final BigDecimal YEAR = BigDecimal.valueOf(12);
    public static final BigDecimal PERCENT = BigDecimal.valueOf(100);

    public static BigDecimal calculateInterestAmount(BigDecimal residualAmount, BigDecimal interestPercent) {
        return residualAmount.multiply(interestPercent).divide(YEAR, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateResidualAmount(BigDecimal amount, RateAmounts rateAmounts) {
        return amount.subtract(rateAmounts.capitalAmount())
            .subtract(rateAmounts.overpayment().amount()).max(BigDecimal.ZERO)
            .setScale(2, RoundingMode.HALF_UP);
    }


    public static BigDecimal calculateQ(BigDecimal interestPercent) {
        return interestPercent.divide(YEAR, 50, RoundingMode.HALF_UP).add(BigDecimal.ONE);
    }

    public static BigDecimal calculateRateAmount(
        BigDecimal referenceAmount,
        BigDecimal q,
        BigDecimal monthsDuration,
        BigDecimal interestAmount,
        BigDecimal residualAmount) {
        BigDecimal rateAmount = (referenceAmount.multiply(q.pow(monthsDuration.intValue()))
            .multiply(q.subtract(BigDecimal.ONE))).divide(q.pow(monthsDuration.intValue()).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        return compareWithResidual(rateAmount, interestAmount, residualAmount);

    }

    public static BigDecimal compareWithResidual(BigDecimal rateAmount, BigDecimal interestAmount,
                                            BigDecimal residualAmount) {
        if (rateAmount.subtract(interestAmount).compareTo(residualAmount) >= 0) {
            return residualAmount.add(interestAmount);
        }
        return rateAmount;
    }

    public static BigDecimal calculateConstantCapitalAmount(BigDecimal rateAmount, BigDecimal interestAmount, BigDecimal residualAmount) {
        BigDecimal capitalAmount = rateAmount.subtract(interestAmount);
        if (capitalAmount.compareTo(residualAmount) >= 0) {
            return residualAmount.setScale(2,RoundingMode.HALF_UP);
        }
        return capitalAmount;
    }

    public static BigDecimal calculateDecreasingCapitalAmount(BigDecimal referenceAmount, BigDecimal monthsDuration) {
        return referenceAmount.divide(monthsDuration, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal compareCapitalWithResidual(final BigDecimal capitalAmount,
                                                       final BigDecimal residualAmount) {
        if (capitalAmount.compareTo(residualAmount) >= 0) {
            return residualAmount.setScale(2,RoundingMode.HALF_UP);
        }
        return capitalAmount;
    }

    public static BigDecimal calculateThisYearDeposit(Map<Integer, BigDecimal> deposit, List<Integer> months) {
        BigDecimal pay;
        BigDecimal totalDeposit = BigDecimal.ZERO;
        for (int i = 0; i < YEAR.intValue(); i++) {
            for (Map.Entry<Integer, BigDecimal> entry : deposit.entrySet()) {
                if (months.get(i).equals(entry.getKey())) {
                    pay = entry.getValue();
                    totalDeposit = totalDeposit.add(pay);
                }
            }
        }
        return totalDeposit;
    }

    public static Map<Integer, BigDecimal> printSchema(Map<Integer, BigDecimal> overPaymentSchema) {
        return new TreeMap<>(overPaymentSchema);
    }
}
