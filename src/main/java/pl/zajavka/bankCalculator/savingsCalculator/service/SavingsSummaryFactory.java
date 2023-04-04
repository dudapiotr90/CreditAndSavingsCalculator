package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsSummary;

import java.math.BigDecimal;
import java.util.List;

public class SavingsSummaryFactory {
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
}
