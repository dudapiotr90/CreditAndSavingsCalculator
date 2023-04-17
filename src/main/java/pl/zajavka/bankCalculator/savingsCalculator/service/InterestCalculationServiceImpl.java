package pl.zajavka.bankCalculator.savingsCalculator.service;

import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.YEAR;
import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.calculateThisYearDeposit;

@Service
public class InterestCalculationServiceImpl implements InterestCalculationService {


    private static BigDecimal getFirstDeposit(SavingsData savingsData) {
        return savingsData.depositSchema().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .limit(1)
            .map(Map.Entry::getValue)
            .reduce((previous, next) -> previous)
            .orElseThrow();
    }

    @Override
    public BigDecimal calculateInterestByYear(SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal interest = calculateThisYearDeposit(savingsData.depositSchema(), timePointSavings.months())
            .multiply(savingsData.depositInterest());
        return interest
            .subtract(interest.multiply(savingsData.tax())).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateInterestByYear(SavingsData savingsData, Savings previousSaving, SavingsTimePoint timePointSavings) {
        BigDecimal interest = (previousSaving.savingsAmount().savingAmount()
            .add(calculateThisYearDeposit(savingsData.depositSchema(), timePointSavings.months())))
            .multiply(savingsData.depositInterest());
        return interest
            .subtract(interest.multiply(savingsData.tax())).setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public BigDecimal calculateInterestByQuarter(SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal interest;
        BigDecimal temporaryAmount = getFirstDeposit(savingsData);
        BigDecimal quarterOfYear = YEAR.divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP);
        BigDecimal interestPercentByQuarter
            = savingsData.depositInterest().divide(quarterOfYear, 10, RoundingMode.HALF_UP);
        for (int i = 0; i < quarterOfYear.intValue(); i++) {
            BigDecimal taxFree = temporaryAmount.multiply(interestPercentByQuarter);
            interest = taxFree
                .subtract(taxFree.multiply(savingsData.tax()));
            temporaryAmount = temporaryAmount.add(interest);

        }
        interest = temporaryAmount.subtract(getFirstDeposit(savingsData)).setScale(2, RoundingMode.HALF_UP);
        return interest;
    }

    @Override
    public BigDecimal calculateInterestByQuarter(SavingsData savingsData, Savings previousSaving, SavingsTimePoint timePointSavings) {
        BigDecimal interest;
        BigDecimal temporaryAmount = previousSaving.savingsAmount().savingAmount();
        BigDecimal quarterOfYear = YEAR.divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP);
        BigDecimal interestPercentByMonth
            = savingsData.depositInterest().divide(quarterOfYear, 10, RoundingMode.HALF_UP);
        for (int i = 0; i < quarterOfYear.intValue(); i++) {
            BigDecimal taxFree = temporaryAmount.multiply(interestPercentByMonth);
            interest = taxFree
                .subtract(taxFree.multiply(savingsData.tax()));
            temporaryAmount = temporaryAmount.add(interest);

        }
        interest = temporaryAmount.subtract(previousSaving.savingsAmount().savingAmount()).setScale(2, RoundingMode.HALF_UP);
        return interest;

    }

    @Override
    public BigDecimal calculateInterestByMonth(SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal interest;
        BigDecimal amount = BigDecimal.ZERO;// = savingsData.getDepositAmount();
        BigDecimal interestPercentByMonth
            = savingsData.depositInterest().divide(YEAR, 10, RoundingMode.HALF_UP);
        List<Integer> months = timePointSavings.months();
        Map<Integer, BigDecimal> deposit = savingsData.depositSchema();
        BigDecimal totalDeposit = calculateThisYearDeposit(deposit, months);
        for (int i = 0; i < YEAR.intValue(); i++) {
            BigDecimal temporaryAmount = BigDecimal.ZERO;
            for (Map.Entry<Integer, BigDecimal> entry : deposit.entrySet()) {
                if (months.get(i).equals(entry.getKey())) {
                    temporaryAmount = temporaryAmount.add(entry.getValue());

                }
            }

            amount = amount.add(temporaryAmount);

            BigDecimal taxFree = amount.multiply(interestPercentByMonth).setScale(2, RoundingMode.HALF_UP);
            interest = taxFree
                .subtract(taxFree.multiply(savingsData.tax())).setScale(2, RoundingMode.HALF_UP);
            amount = amount.add(interest);

        }
        interest = amount.subtract(totalDeposit).setScale(2, RoundingMode.HALF_UP);//(savingsData.getDepositAmount());
        return interest;
    }

    @Override
    public BigDecimal calculateInterestByMonth(SavingsData savingsData, Savings previousSaving, SavingsTimePoint timePointSavings) {
        BigDecimal interest;
        BigDecimal amount = previousSaving.savingsAmount().savingAmount();
        BigDecimal interestPercentByMonth
            = savingsData.depositInterest().divide(YEAR, 10, RoundingMode.HALF_UP);

        List<Integer> months = timePointSavings.months();
        Map<Integer, BigDecimal> deposit = savingsData.depositSchema();

        BigDecimal totalDeposit = calculateThisYearDeposit(deposit, months).add(amount);

        for (int i = 0; i < YEAR.intValue(); i++) {
            BigDecimal temporaryAmount = BigDecimal.ZERO;
            for (Map.Entry<Integer, BigDecimal> entry : deposit.entrySet()) {
                if (months.get(i).equals(entry.getKey())) {
                    temporaryAmount = temporaryAmount.add(entry.getValue());

                }
            }
            amount = amount.add(temporaryAmount);

            BigDecimal taxFree = amount.multiply(interestPercentByMonth);
            interest = taxFree
                .subtract(taxFree.multiply(savingsData.tax()));
            amount = amount.add(interest);

        }
        interest = amount.subtract(totalDeposit).setScale(2, RoundingMode.HALF_UP);
        return interest;
    }
}
