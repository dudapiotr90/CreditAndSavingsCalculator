package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class RateCalculationServiceImpl implements RateCalculationService {

    private final TimePointCalculationService timePointCalculationService;
    private final ResidualCalculationService residualCalculationService;
    private final OverpaymentCalculationService overpaymentCalculationService;
    private final RateAmountsCalculationService rateAmountsCalculationService;
    private final ReferenceCalculationService referenceCalculationService;

    private static boolean mortgageFinished(Rate nextRate) {
        BigDecimal toCompare = nextRate.mortgageResidual().amount().setScale(0, RoundingMode.HALF_UP);
        return BigDecimal.ZERO.equals(toCompare);
    }

    @Override
    public List<Rate> calculate(MortgageData mortgageData) {
        List<Rate> rates = new LinkedList<>();

        BigDecimal rateNumber = BigDecimal.ONE;

        Rate firstRate = calculateRate(rateNumber, mortgageData);
        log.trace("FirstRate: [{}]",firstRate);
        rates.add(firstRate);

        Rate previousRate = firstRate;

        for (
            BigDecimal rateNumberIndex = rateNumber.add(BigDecimal.ONE);
            rateNumberIndex.compareTo(mortgageData.monthsDuration()) <= 0;
            rateNumberIndex = rateNumberIndex.add(BigDecimal.ONE)) {

            Rate nextRate = calculateRate(rateNumberIndex, mortgageData, previousRate);
            log.trace("NextRate: [{}]",nextRate);
            rates.add(nextRate);
            previousRate = nextRate;

            if (mortgageFinished(nextRate)) {
                break;
            }

        }
        return rates;
    }

    private Rate calculateRate(BigDecimal rateNumberIndex, MortgageData mortgageData) {
        MortgageTimePoint mortgageTimePoint = timePointCalculationService.calculate(rateNumberIndex, mortgageData);
        Overpayment overpayment = overpaymentCalculationService.calculate(rateNumberIndex, mortgageData);
        RateAmounts rateAmounts = rateAmountsCalculationService.calculate(mortgageData, overpayment);
        MortgageResidual mortgageResidual = residualCalculationService.calculate(rateAmounts, mortgageData);
        MortgageReference mortgageReference = referenceCalculationService.calculate(mortgageData, overpayment);

        return new Rate(
            rateNumberIndex,
            mortgageTimePoint,
            rateAmounts,
            mortgageResidual,
            mortgageReference
        );
    }

    private Rate calculateRate(BigDecimal rateNumberIndex, MortgageData mortgageData, Rate previousRate) {
        MortgageTimePoint mortgageTimePoint = timePointCalculationService.calculate(rateNumberIndex, previousRate);
        Overpayment overpayment = overpaymentCalculationService.calculate(rateNumberIndex, mortgageData);
        RateAmounts rateAmounts = rateAmountsCalculationService.calculate(mortgageData, overpayment, previousRate);
        MortgageResidual mortgageResidual = residualCalculationService
            .calculate(rateAmounts, previousRate, mortgageData);
        MortgageReference mortgageReference = referenceCalculationService
            .calculate(mortgageData, rateAmounts, previousRate);

        return new Rate(
            rateNumberIndex,
            mortgageTimePoint,
            rateAmounts,
            mortgageResidual,
            mortgageReference
        );

    }
}
