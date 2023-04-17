package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.RateAmounts;
@Slf4j
@Service
@RequiredArgsConstructor
public class RateAmountsCalculationServiceImpl implements RateAmountsCalculationService {

    private final ConstantAmountsCalculationService constantAmountsCalculationService;
    private final DecreasingAmountsCalculationService decreasingAmountsCalculationService;


    @Override
    public RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment) {
        switch (mortgageData.rateType()) {
            case CONSTANT:
                return constantAmountsCalculationService.calculate(mortgageData, overpayment);
            case DECREASING:
                return decreasingAmountsCalculationService.calculate(mortgageData, overpayment);
            default:
                log.error("InputData.rateType(): [{}]", mortgageData.rateType());
                throw new MortgageException();

        }
    }


    @Override
    public RateAmounts calculate(MortgageData mortgageData, Overpayment overpayment, Rate previousRate) {
        switch (mortgageData.rateType()) {
            case CONSTANT:
                return constantAmountsCalculationService.calculate(mortgageData, overpayment, previousRate);
            case DECREASING:
                return decreasingAmountsCalculationService.calculate(mortgageData, overpayment, previousRate);
            default:
                log.error("InputData.rateType(): [{}]", mortgageData.rateType());
                throw new MortgageException();
        }
    }
}



