package pl.zajavka.bankCalculator.calculators.savingsCalculator.service;

import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.CalculatingMethods;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

@Service
public class AmountCalculationServiceImpl implements AmountCalculationService {

    @Override
    public BigDecimal calculateSavingAmount(
        SavingsData savingsData, BigDecimal interestAmount, SavingsTimePoint savingsTimePoint) {

        return CalculatingMethods.calculateThisYearDeposit(
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
            .add(CalculatingMethods.calculateThisYearDeposit(savingsData.depositSchema(), savingsTimePoint.months()));
    }


}
