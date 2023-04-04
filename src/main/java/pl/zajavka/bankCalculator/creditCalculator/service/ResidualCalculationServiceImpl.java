package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.calculateQ;
import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.calculateResidualAmount;

public class ResidualCalculationServiceImpl implements ResidualCalculationService {

    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, InputData inputData) {
        if (BigDecimal.ZERO.equals(inputData.amount())) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        BigDecimal residualAmount = calculateResidualAmount(inputData.amount(), rateAmounts);
        BigDecimal residualDuration = calculateResidualDuration(inputData, inputData.monthsDuration(), residualAmount, rateAmounts);

        return new MortgageResidual(residualAmount, residualDuration);
    }

    @Override
    public MortgageResidual calculate(
        RateAmounts rateAmounts,
        Rate previousRate,
        InputData inputData
    ) {
        BigDecimal previousResidualAmount = previousRate.mortgageResidual().amount();
        BigDecimal previousResidualDuration = previousRate.mortgageResidual().duration();
        if (BigDecimal.ZERO.equals(previousResidualAmount)) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        BigDecimal residualAmount = calculateResidualAmount((previousResidualAmount), rateAmounts);
        BigDecimal residualDuration = calculateResidualDuration(inputData, previousResidualDuration, residualAmount, rateAmounts);
        return new MortgageResidual(residualAmount, residualDuration);
    }

    private BigDecimal calculateResidualDuration(
        InputData inputData,
        BigDecimal residualDuration,
        BigDecimal residualAmount,
        RateAmounts rateAmounts
    ) {
        if (Overpayment.REDUCE_PERIOD.equals(inputData.overpaymentReduceWay())) {
            return switch (inputData.rateType()) {
                case CONSTANT -> calculateConstantResidualDuration(inputData, residualAmount, rateAmounts);
                case DECREASING -> calculateDecreasingResidualDuration(residualAmount, rateAmounts);
            };

        }
        return residualDuration.subtract(BigDecimal.ONE);
    }

    private static BigDecimal calculateConstantResidualDuration(
        InputData inputData,
        BigDecimal residualAmount,
        RateAmounts rateAmounts
    ) {
        BigDecimal residualDuration;
        BigDecimal q = calculateQ(inputData.interestPercent());
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


