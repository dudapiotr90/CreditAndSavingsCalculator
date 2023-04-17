package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.CalculatingMethods;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.RateAmounts;

import java.math.BigDecimal;

@Slf4j
@Service
public class DecreasingAmountsCalculationServiceImpl implements DecreasingAmountsCalculationService {

    @Override
    public RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment) {
        BigDecimal interestPercent = mortgageData.interestPercent();
        log.info("InterestPercent: [{}]", interestPercent);
        BigDecimal residualAmount = mortgageData.amount();
        log.info("ResidualAmount: [{}]", residualAmount);
        BigDecimal referenceAmount = mortgageData.amount();
        log.info("ReferenceAmount: [{}]", referenceAmount);
        BigDecimal referenceDuration = mortgageData.monthsDuration();
        log.info("ReferenceDuration: [{}]", referenceDuration);


        BigDecimal interestAmount = CalculatingMethods.calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]", interestAmount);
        BigDecimal capitalAmount = CalculatingMethods.compareCapitalWithResidual(CalculatingMethods.calculateDecreasingCapitalAmount(referenceAmount, referenceDuration), residualAmount);
        log.info("CapitalAmount: [{}]", capitalAmount);
        BigDecimal rateAmount = capitalAmount.add(interestAmount);
        log.info("RateAmount: [{}]", rateAmount);


        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment, Rate previousRate) {
        BigDecimal interestPercent = mortgageData.interestPercent();
        log.info("InterestPercent: [{}]", interestPercent);

        BigDecimal residualAmount = previousRate.mortgageResidual().amount();
        log.info("ResidualAmount: [{}]", residualAmount);
        BigDecimal referenceAmount = previousRate.mortgageReference().amount();
        log.info("ReferenceAmount: [{}]", referenceAmount);
        BigDecimal referenceDuration = previousRate.mortgageReference().duration();
        log.info("ReferenceDuration: [{}]", referenceDuration);

        BigDecimal interestAmount = CalculatingMethods.calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]", interestAmount);
        BigDecimal capitalAmount = CalculatingMethods.compareCapitalWithResidual(CalculatingMethods.calculateDecreasingCapitalAmount(referenceAmount, referenceDuration), residualAmount);

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
