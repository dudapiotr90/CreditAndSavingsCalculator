package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.CalculatingMethods;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ResidualCalculationServiceImpl implements ResidualCalculationService {

    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, MortgageData mortgageData) {
        if (BigDecimal.ZERO.equals(mortgageData.amount())) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        BigDecimal residualAmount = CalculatingMethods.calculateResidualAmount(mortgageData.amount(), rateAmounts);
        BigDecimal residualDuration = calculateResidualDuration(mortgageData, mortgageData.monthsDuration(), residualAmount, rateAmounts);

        return new MortgageResidual(residualAmount, residualDuration);
    }

    @Override
    public MortgageResidual calculate(
        RateAmounts rateAmounts,
        Rate previousRate,
        MortgageData mortgageData
    ) {
        BigDecimal previousResidualAmount = previousRate.mortgageResidual().amount();
        BigDecimal previousResidualDuration = previousRate.mortgageResidual().duration();
        if (BigDecimal.ZERO.equals(previousResidualAmount)) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        BigDecimal residualAmount = CalculatingMethods.calculateResidualAmount((previousResidualAmount), rateAmounts);
        BigDecimal residualDuration = calculateResidualDuration(mortgageData, previousResidualDuration, residualAmount, rateAmounts);
        return new MortgageResidual(residualAmount, residualDuration);
    }

    private BigDecimal calculateResidualDuration(
        MortgageData mortgageData,
        BigDecimal residualDuration,
        BigDecimal residualAmount,
        RateAmounts rateAmounts
    ) {
        if (Overpayment.REDUCE_PERIOD.equals(mortgageData.overpaymentReduceWay())) {
            return switch (mortgageData.rateType()) {
                case CONSTANT -> calculateConstantResidualDuration(mortgageData, residualAmount, rateAmounts);
                case DECREASING -> calculateDecreasingResidualDuration(residualAmount, rateAmounts);
            };

        }
        return residualDuration.subtract(BigDecimal.ONE);
    }

    private static BigDecimal calculateConstantResidualDuration(
        MortgageData mortgageData,
        BigDecimal residualAmount,
        RateAmounts rateAmounts
    ) {
        BigDecimal residualDuration;
        BigDecimal q = CalculatingMethods.calculateQ(mortgageData.interestPercent());
        BigDecimal counterLog = rateAmounts.rateAmount();
        BigDecimal denominator = rateAmounts.rateAmount()
            .subtract(q.multiply(residualAmount)).add(residualAmount);

        BigDecimal mainCounterLogar = BigDecimal.valueOf(
            Math.log(counterLog.divide(denominator, 50, RoundingMode.HALF_UP).doubleValue()));
        BigDecimal mainDenominator = BigDecimal.valueOf(Math.log(q.doubleValue()));

        residualDuration = mainCounterLogar.divide(mainDenominator, 0, RoundingMode.UP);

        return residualDuration;
    }

    private static BigDecimal calculateDecreasingResidualDuration(
        BigDecimal residualAmount,
        RateAmounts rateAmounts
    ) {
        return residualAmount.divide(rateAmounts.capitalAmount(), 0, RoundingMode.UP);
    }
}


