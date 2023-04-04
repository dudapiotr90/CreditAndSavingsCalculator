package pl.zajavka.bankCalculator.creditCalculator.service;

import lombok.extern.slf4j.Slf4j;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateAmounts;

import java.math.BigDecimal;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.*;

@Slf4j
public class ConstantAmountsCalculationServiceImpl implements
    ConstantAmountsCalculationService {

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment) {
        BigDecimal interestPercent = inputData.interestPercent();
        log.info("InterestPercent: [{}]", interestPercent);
        BigDecimal q = calculateQ(interestPercent);
        log.debug("Q: [{}]", q);

        BigDecimal residualAmount = BigDecimal.ZERO;
        BigDecimal referenceAmount = BigDecimal.ZERO;
        BigDecimal referenceDuration = inputData.monthsDuration();
        log.info("ReferenceDuration: [{}]", referenceDuration);

        switch (inputData.overpaymentReduceWay()) {
            case Overpayment.REDUCE_RATE -> {
                residualAmount = inputData.amount().subtract(overpayment.amount());
                referenceAmount = inputData.amount().subtract(overpayment.amount());
            }
            case Overpayment.REDUCE_PERIOD -> {
                residualAmount = inputData.amount();
                referenceAmount = inputData.amount();
            }
        }
        log.info("ResidualAmount: [{}]", residualAmount);
        log.info("ReferenceAmount: [{}]", referenceAmount);
        BigDecimal interestAmount = calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]",interestAmount);
        BigDecimal rateAmount = calculateRateAmount(
            referenceAmount, q, referenceDuration, interestAmount, residualAmount);
        log.info("RateAmount: [{}]",rateAmount);
        BigDecimal capitalAmount = calculateConstantCapitalAmount(rateAmount, interestAmount, residualAmount);
        log.info("CapitalAmount: [{}]",capitalAmount);


        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate) {
        BigDecimal interestPercent = inputData.interestPercent();
        log.info("InterestPercent: [{}]",interestPercent);
        BigDecimal q = calculateQ(interestPercent);
        log.debug("Q: [{}]",q );

        BigDecimal residualAmount = previousRate.mortgageResidual().amount();
        log.info("ResidualAmount: [{}]",residualAmount);
        BigDecimal referenceAmount = previousRate.mortgageReference().amount();
        log.info("ReferenceAmount: [{}]",referenceAmount);
        BigDecimal referenceDuration = previousRate.mortgageReference().duration();
        log.info("ReferenceDuration: [{}]",referenceDuration);

        BigDecimal interestAmount = calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]",interestAmount);
        BigDecimal rateAmount = calculateRateAmount(referenceAmount, q, referenceDuration, interestAmount, residualAmount);
        log.info("RateAmount: [{}]",rateAmount);
        BigDecimal capitalAmount = calculateConstantCapitalAmount(rateAmount, interestAmount, residualAmount);
        log.info("CapitalAmount: [{}]",capitalAmount);


        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }
}
