package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

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
public class ConstantAmountsCalculationServiceImpl implements
    ConstantAmountsCalculationService {

    @Override
    public RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment) {
        BigDecimal interestPercent = mortgageData.interestPercent();
        log.info("InterestPercent: [{}]", interestPercent);
        BigDecimal q = CalculatingMethods.calculateQ(interestPercent);
        log.debug("Q: [{}]", q);

        BigDecimal residualAmount = BigDecimal.ZERO;
        BigDecimal referenceAmount = BigDecimal.ZERO;
        BigDecimal referenceDuration = mortgageData.monthsDuration();
        log.info("ReferenceDuration: [{}]", referenceDuration);

        switch (mortgageData.overpaymentReduceWay()) {
            case Overpayment.REDUCE_RATE -> {
                residualAmount = mortgageData.amount().subtract(overpayment.amount());
                referenceAmount = mortgageData.amount().subtract(overpayment.amount());
            }
            case Overpayment.REDUCE_PERIOD -> {
                residualAmount = mortgageData.amount();
                referenceAmount = mortgageData.amount();
            }
        }
        log.info("ResidualAmount: [{}]", residualAmount);
        log.info("ReferenceAmount: [{}]", referenceAmount);
        BigDecimal interestAmount = CalculatingMethods.calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]",interestAmount);
        BigDecimal rateAmount = CalculatingMethods.calculateRateAmount(
            referenceAmount, q, referenceDuration, interestAmount, residualAmount);
        log.info("RateAmount: [{}]",rateAmount);
        BigDecimal capitalAmount = CalculatingMethods.calculateConstantCapitalAmount(rateAmount, interestAmount, residualAmount);
        log.info("CapitalAmount: [{}]",capitalAmount);


        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment, Rate previousRate) {
        BigDecimal interestPercent = mortgageData.interestPercent();
        log.info("InterestPercent: [{}]",interestPercent);
        BigDecimal q = CalculatingMethods.calculateQ(interestPercent);
        log.debug("Q: [{}]",q );

        BigDecimal residualAmount = previousRate.mortgageResidual().amount();
        log.info("ResidualAmount: [{}]",residualAmount);
        BigDecimal referenceAmount = previousRate.mortgageReference().amount();
        log.info("ReferenceAmount: [{}]",referenceAmount);
        BigDecimal referenceDuration = previousRate.mortgageReference().duration();
        log.info("ReferenceDuration: [{}]",referenceDuration);

        BigDecimal interestAmount = CalculatingMethods.calculateInterestAmount(residualAmount, interestPercent);
        log.info("InterestAmount: [{}]",interestAmount);
        BigDecimal rateAmount = CalculatingMethods.calculateRateAmount(referenceAmount, q, referenceDuration, interestAmount, residualAmount);
        log.info("RateAmount: [{}]",rateAmount);
        BigDecimal capitalAmount = CalculatingMethods.calculateConstantCapitalAmount(rateAmount, interestAmount, residualAmount);
        log.info("CapitalAmount: [{}]",capitalAmount);


        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }
}
