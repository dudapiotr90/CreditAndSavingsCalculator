package pl.zajavka.bankCalculator.creditCalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateAmounts;
@Slf4j
@RequiredArgsConstructor
public class RateAmountsCalculationServiceImpl implements RateAmountsCalculationService {

    private final ConstantAmountsCalculationService constantAmountsCalculationService;
    private final DecreasingAmountsCalculationService decreasingAmountsCalculationService;


    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment) {
        switch (inputData.rateType()) {
            case CONSTANT:
                return constantAmountsCalculationService.calculate(inputData, overpayment);
            case DECREASING:
                return decreasingAmountsCalculationService.calculate(inputData, overpayment);
            default:
                log.error("InputData.rateType(): [{}]",inputData.rateType());
                throw new MortgageException();

        }
    }


    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate) {
        switch (inputData.rateType()) {
            case CONSTANT:
                return constantAmountsCalculationService.calculate(inputData, overpayment, previousRate);
            case DECREASING:
                return decreasingAmountsCalculationService.calculate(inputData, overpayment, previousRate);
            default:
                log.error("InputData.rateType(): [{}]",inputData.rateType());
                throw new MortgageException();
        }
    }
}



