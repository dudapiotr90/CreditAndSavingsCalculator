package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.Comparison;
import pl.zajavka.bankCalculator.calculators.commonServices.PrintingService;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;

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
    public void calculate(MortgageData mortgageData) {
        log.info("MortgageData: [{}]", mortgageData);
        printingService.printIntroInformation(mortgageData);

        List<Rate> rates = rateCalculationService.calculate(mortgageData);
        rates.forEach(rate -> log.debug("Rate: [{}]", rate));

        List<Rate> noOverpaymentRates = rateCalculationService.calculate(mortgageData.withOverpaymentSchema(Collections.emptyMap()));
        noOverpaymentRates.forEach(rate -> log.debug("NoOverpaymentRate: [{}]", rate));

        MortgageSummary summary = mortgageSummaryService.calculate(rates);

        MortgageSummary withoutOverpaymentSummary = mortgageSummaryService.calculate(noOverpaymentRates);

        Comparison instance = Comparison.getInstance(summary, withoutOverpaymentSummary, null);
        log.warn("Preparing Comparison instance [{}]", instance);

        printingService.printMortgageSummary(summary);

        printingService.printOverpaymentProfit(withoutOverpaymentSummary, summary);

        printingService.printRatesSchedule(rates, mortgageData);

    }
}
