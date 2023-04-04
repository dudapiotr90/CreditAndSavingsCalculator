package pl.zajavka.bankCalculator.savingsCalculator.service;

import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.calculateThisYearDeposit;

public class AmountCalculationServiceImpl implements AmountCalculationService {

    @Override
    public BigDecimal calculateSavingAmount(
        SavingsData savingsData, BigDecimal interestAmount, SavingsTimePoint savingsTimePoint) {

        return calculateThisYearDeposit(
            savingsData.depositSchema(), savingsTimePoint.months()).add(interestAmount);
    }

    @Override
    public BigDecimal calculateSavingAmount(
        SavingsData savingsData,
        BigDecimal interestAmount,
        Savings previousSaving,
        SavingsTimePoint savingsTimePoint
    ) {
        return previousSaving.savingsAmount().savingAmount().add(interestAmount)
            .add(calculateThisYearDeposit(savingsData.depositSchema(), savingsTimePoint.months()));
    }


}
