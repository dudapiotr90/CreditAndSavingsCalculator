package pl.zajavka.bankCalculator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.RateAmounts;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.Function;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.MortgageSummaryService;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsSummary;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.services.SavingFunction;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.services.SavingsSummaryService;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "pl.zajavka")
public class BankCalculatorConfiguration {

    @Bean
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
    @Bean
    public static SavingsSummaryService createSavings() {
        return savings -> {
            BigDecimal earnings = calculate(savings,
                interest -> interest.savingsAmount().interestAmount());
            BigDecimal savingsSum = calculateSum(savings);
            return new SavingsSummary(earnings, savingsSum);
        };

    }

    private static BigDecimal calculateSum(List<Savings> savings) {
        return savings.get(savings.size() - 1).savingsAmount().savingAmount();
    }


    private static BigDecimal calculate(List<Savings> savings, SavingFunction function) {
        BigDecimal savingsSum = BigDecimal.ZERO;
        for (Savings saving : savings) {
            savingsSum = savingsSum.add(function.calculate(saving));
        }
        return savingsSum;
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
