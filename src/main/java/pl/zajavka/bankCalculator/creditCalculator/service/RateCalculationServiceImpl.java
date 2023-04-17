package pl.zajavka.bankCalculator.creditCalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.*;

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
    public List<Rate> calculate(InputData inputData) {
        List<Rate> rates = new LinkedList<>();

        BigDecimal rateNumber = BigDecimal.ONE;

        Rate firstRate = calculateRate(rateNumber, inputData);
        log.trace("FirstRate: [{}]",firstRate);
        rates.add(firstRate);

        Rate previousRate = firstRate;

        for (
            BigDecimal rateNumberIndex = rateNumber.add(BigDecimal.ONE);
            rateNumberIndex.compareTo(inputData.monthsDuration()) <= 0;
            rateNumberIndex = rateNumberIndex.add(BigDecimal.ONE)) {

            Rate nextRate = calculateRate(rateNumberIndex, inputData, previousRate);
            log.trace("NextRate: [{}]",nextRate);
            rates.add(nextRate);
            previousRate = nextRate;

            if (mortgageFinished(nextRate)) {
                break;
            }

        }
        return rates;
    }

    private Rate calculateRate(BigDecimal rateNumberIndex, InputData inputData) {
        MortgageTimePoint mortgageTimePoint = timePointCalculationService.calculate(rateNumberIndex, inputData);
        Overpayment overpayment = overpaymentCalculationService.calculate(rateNumberIndex, inputData);
        RateAmounts rateAmounts = rateAmountsCalculationService.calculate(inputData, overpayment);
        MortgageResidual mortgageResidual = residualCalculationService.calculate(rateAmounts, inputData);
        MortgageReference mortgageReference = referenceCalculationService.calculate(inputData, overpayment);

        return new Rate(
            rateNumberIndex,
            mortgageTimePoint,
            rateAmounts,
            mortgageResidual,
            mortgageReference
        );
    }

    private Rate calculateRate(BigDecimal rateNumberIndex, InputData inputData, Rate previousRate) {
        MortgageTimePoint mortgageTimePoint = timePointCalculationService.calculate(rateNumberIndex, previousRate);
        Overpayment overpayment = overpaymentCalculationService.calculate(rateNumberIndex, inputData);
        RateAmounts rateAmounts = rateAmountsCalculationService.calculate(inputData, overpayment, previousRate);
        MortgageResidual mortgageResidual = residualCalculationService
            .calculate(rateAmounts, previousRate, inputData);
        MortgageReference mortgageReference = referenceCalculationService
            .calculate(inputData, rateAmounts, previousRate);

        return new Rate(
            rateNumberIndex,
            mortgageTimePoint,
            rateAmounts,
            mortgageResidual,
            mortgageReference
        );

    }
}
