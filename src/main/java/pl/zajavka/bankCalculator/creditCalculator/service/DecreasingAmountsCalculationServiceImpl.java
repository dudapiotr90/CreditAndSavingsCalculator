package pl.zajavka.bankCalculator.creditCalculator.service;

import lombok.extern.slf4j.Slf4j;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateAmounts;

import java.math.BigDecimal;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.*;

@Slf4j
public class DecreasingAmountsCalculationServiceImpl implements DecreasingAmountsCalculationService {

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment) {
        BigDecimal interestPercent = inputData.interestPercent();
        log.info("InterestPercent: [{}]", interestPercent);
        BigDecimal residualAmount = inputData.amount();
        log.info("ResidualAmount: [{}]", residualAmount);
        BigDecimal referenceAmount = inputData.amount();
        log.info("ReferenceAmount: [{}]", referenceAmount);
        BigDecimal referenceDuration = inputData.monthsDuration();
        log.info("ReferenceDuration: [{}]", referenceDuration);


        BigDecimal interestAmount = calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]", interestAmount);
        BigDecimal capitalAmount = compareCapitalWithResidual(calculateDecreasingCapitalAmount(referenceAmount, referenceDuration), residualAmount);
        log.info("CapitalAmount: [{}]", capitalAmount);
        BigDecimal rateAmount = capitalAmount.add(interestAmount);
        log.info("RateAmount: [{}]", rateAmount);


        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate) {
        BigDecimal interestPercent = inputData.interestPercent();
        log.info("InterestPercent: [{}]", interestPercent);

        BigDecimal residualAmount = previousRate.mortgageResidual().amount();
        log.info("ResidualAmount: [{}]", residualAmount);
        BigDecimal referenceAmount = previousRate.mortgageReference().amount();
        log.info("ReferenceAmount: [{}]", referenceAmount);
        BigDecimal referenceDuration = previousRate.mortgageReference().duration();
        log.info("ReferenceDuration: [{}]", referenceDuration);

        BigDecimal interestAmount = calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]", interestAmount);
        BigDecimal capitalAmount = compareCapitalWithResidual(calculateDecreasingCapitalAmount(referenceAmount, referenceDuration), residualAmount);

        // verification for rounding inaccuracy
        if (previousRate.mortgageResidual().amount().subtract(capitalAmount).compareTo(BigDecimal.ONE) < 0) {
            capitalAmount = previousRate.mortgageResidual().amount();
        }

        log.info("CapitalAmount: [{}]", capitalAmount);
        BigDecimal rateAmount = capitalAmount.add(interestAmount);
        log.info("RateAmount: [{}]", rateAmount);

        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

}
