package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.CalculatingMethods;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class ReferenceCalculationServiceImpl implements ReferenceCalculationService {


    @Override
    public MortgageReference calculate(MortgageData mortgageData, Overpayment overpayment) {
        if (BigDecimal.ZERO.equals(mortgageData.amount())) {
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        if (Overpayment.REDUCE_RATE.equals(mortgageData.overpaymentReduceWay())) {
            return new MortgageReference(
                mortgageData.amount().subtract(overpayment.amount()), mortgageData.monthsDuration());
        }
        return new MortgageReference(mortgageData.amount(), mortgageData.monthsDuration());
    }

    @Override
    public MortgageReference calculate(MortgageData mortgageData, RateAmounts rateAmounts, Rate previousRate) {
        if (BigDecimal.ZERO.equals(previousRate.mortgageResidual().amount())) {
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        switch (mortgageData.overpaymentReduceWay()) {
            case Overpayment.REDUCE_PERIOD:
                return new MortgageReference(mortgageData.amount(), mortgageData.monthsDuration());
            case Overpayment.REDUCE_RATE:
                return reduceRateMortgageReference(rateAmounts, previousRate);
            default:
                log.error("InputData.overpaymentReduceWay(): [{}]", mortgageData.overpaymentReduceWay());
                throw new MortgageException();
        }
    }

    private MortgageReference reduceRateMortgageReference(RateAmounts rateAmounts, Rate previousRate) {
        if (rateAmounts.overpayment().amount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal referenceAmount = CalculatingMethods.calculateResidualAmount(
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
