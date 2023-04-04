package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateAmounts;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageSummary;

import java.math.BigDecimal;
import java.util.List;

public class SummaryServiceFactory {
    public static MortgageSummaryService create() {
        return rates -> {
            BigDecimal interestSum = calculate(rates,
                rate -> rate.rateAmounts().interest());
            BigDecimal provisions = calculate(rates,
                rate -> rate.rateAmounts().overpayment().provisionAmount());
            BigDecimal totalLosses = interestSum.add(provisions);
            BigDecimal totalCapital = calculate(rates, rate -> totalCapital(rate.rateAmounts()));
            return new MortgageSummary(interestSum, provisions, totalLosses,totalCapital);
        };
    }

    private static BigDecimal totalCapital(RateAmounts rateAmounts) {
        return rateAmounts.capitalAmount().add(rateAmounts.overpayment().amount());
    }

    private static BigDecimal calculate(List<Rate> rates, Function function) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Rate rate : rates) {
            sum = sum.add(function.calculate(rate));
        }
        return sum;
    }

}
