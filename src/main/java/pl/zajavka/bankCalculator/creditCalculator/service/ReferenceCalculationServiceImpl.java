package pl.zajavka.bankCalculator.creditCalculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.calculateResidualAmount;
@Slf4j
@Service
public class ReferenceCalculationServiceImpl implements ReferenceCalculationService {


    @Override
    public MortgageReference calculate(InputData inputData, Overpayment overpayment) {
        if (BigDecimal.ZERO.equals(inputData.amount())) {
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        if (Overpayment.REDUCE_RATE.equals(inputData.overpaymentReduceWay())) {
            return new MortgageReference(
                inputData.amount().subtract(overpayment.amount()), inputData.monthsDuration());
        }
        return new MortgageReference(inputData.amount(), inputData.monthsDuration());
    }

    @Override
    public MortgageReference calculate(InputData inputData, RateAmounts rateAmounts, Rate previousRate) {
        if (BigDecimal.ZERO.equals(previousRate.mortgageResidual().amount())) {
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        switch (inputData.overpaymentReduceWay()) {
            case Overpayment.REDUCE_PERIOD:
                return new MortgageReference(inputData.amount(), inputData.monthsDuration());
            case Overpayment.REDUCE_RATE:
                return reduceRateMortgageReference(rateAmounts, previousRate);
            default:
                log.error("InputData.overpaymentReduceWay(): [{}]",inputData.overpaymentReduceWay());
                throw new MortgageException();
        }
    }

    private MortgageReference reduceRateMortgageReference(RateAmounts rateAmounts, Rate previousRate) {
        if (rateAmounts.overpayment().amount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal referenceAmount = calculateResidualAmount(
                previousRate.mortgageResidual().amount(), rateAmounts)
                .setScale(2, RoundingMode.HALF_UP);
            BigDecimal referenceDuration =
                previousRate.mortgageResidual().duration().subtract(BigDecimal.ONE)
                    .setScale(1, RoundingMode.HALF_UP);
            return new MortgageReference(referenceAmount, referenceDuration);
        }

        return new MortgageReference(
            previousRate.mortgageReference().amount().setScale(2, RoundingMode.HALF_UP),
            previousRate.mortgageReference().duration());
    }


}
