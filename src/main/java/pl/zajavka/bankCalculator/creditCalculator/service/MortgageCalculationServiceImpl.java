package pl.zajavka.bankCalculator.creditCalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.globalServices.Comparison;
import pl.zajavka.bankCalculator.globalServices.PrintingService;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MortgageCalculationServiceImpl implements MortgageCalculationService {
    private final PrintingService printingService;
    private final RateCalculationService rateCalculationService;
    private final MortgageSummaryService mortgageSummaryService;

    @Override
    public void calculate(InputData inputData) {
        log.info("MortgageData: [{}]", inputData);
        printingService.printIntroInformation(inputData);

        List<Rate> rates = rateCalculationService.calculate(inputData);
        rates.forEach(rate -> log.debug("Rate: [{}]", rate));

        List<Rate> noOverpaymentRates = rateCalculationService.calculate(inputData.withOverpaymentSchema(Collections.emptyMap()));
        noOverpaymentRates.forEach(rate -> log.debug("NoOverpaymentRate: [{}]", rate));

        MortgageSummary summary = mortgageSummaryService.calculate(rates);

        MortgageSummary withoutOverpaymentSummary = mortgageSummaryService.calculate(noOverpaymentRates);

        Comparison instance = Comparison.getInstance(summary, withoutOverpaymentSummary, null);
        log.warn("Preparing Comparison instance [{}]", instance);

        printingService.printMortgageSummary(summary);

        printingService.printOverpaymentProfit(withoutOverpaymentSummary, summary);

        printingService.printRatesSchedule(rates, inputData);

    }
}
