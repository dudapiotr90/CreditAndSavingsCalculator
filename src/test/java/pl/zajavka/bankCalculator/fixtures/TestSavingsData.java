package pl.zajavka.bankCalculator.fixtures;

import pl.zajavka.bankCalculator.calculators.commonServices.Comparison;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TestSavingsData {

    public static SavingsData someSavingsData() {
        return SavingsData.builder()
            .depositDuration(BigDecimal.valueOf(30))
            .savingType(SavingsType.RENEWABLE)
            .depositInterest(BigDecimal.valueOf(5))
            .interestCapitalization(InterestCapitalization.AFTER_MONTH)
            .tax(BigDecimal.valueOf(22))
            .depositSchema(Map.of(
                1, BigDecimal.valueOf(4578.93),
                12, BigDecimal.valueOf(3579.64),
                28, BigDecimal.valueOf(12247.32),
                44, BigDecimal.valueOf(6478.71)
            ))
            .build();
    }

    public static SavingsTimePoint someSavingsTimePoint() {
        return SavingsTimePoint.builder()
            .date(LocalDate.of(2017, 6, 8))
            .year(BigDecimal.valueOf(4))
            .months(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
            .build();
    }

    public static Savings someSavings() {
        return Savings.builder()
            .savingsAmount(someSavingsAmount())
            .savingsTimePoint(someSavingsTimePoint())
            .savingsNumber(BigDecimal.valueOf(13))
            .build();
    }

    public static SavingsAmount someSavingsAmount() {
        return SavingsAmount.builder()
            .savingAmount(BigDecimal.valueOf(14957.12))
            .interestAmount(BigDecimal.valueOf(975.77))
            .build();
    }

    public static List<Savings> someSavingRates() {
        return IntStream.rangeClosed(1, 15).boxed()
            .map(i -> someSavings().withSavingsNumber(BigDecimal.valueOf(i)))
            .toList();
    }

    public static SavingsSummary someSavingsSummary() {
        return SavingsSummary.builder()
            .savingsSum(BigDecimal.valueOf(13679))
            .earnings(BigDecimal.valueOf(4632))
            .build();
    }

    public static Comparison someComparison() {
        return Comparison.builder()
            .mortgageSummary(TestMortgageData.someMortgageSummary())
            .withoutOverpaymentMortgageSummary(TestMortgageData.someMortgageSummary())
            .build();
    }

}
