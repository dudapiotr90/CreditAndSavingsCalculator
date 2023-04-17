package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.CalculatingMethods;
import pl.zajavka.bankCalculator.calculators.commonServices.Comparison;
import pl.zajavka.bankCalculator.calculators.commonServices.PrintingService;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsSummary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingsCalculationServiceImpl implements SavingsCalculationService {

    private final PrintingService printingService;
    private final SavingsAmountCalculationService savingsAmountCalculationService;
    private final SavingsSummaryService savingsSummaryService;


    @Override
    public void calculate(SavingsData savingsData, MortgageData mortgageData) {
        log.info("SavingsData: [{}]", savingsData);

        List<Savings> savings = calculateSavings(savingsData, mortgageData);
        savings.forEach(saving -> log.debug("Saving: [{}]", saving));

        SavingsSummary summarySavings = savingsSummaryService.calculateSavings(savings);

        Comparison comparison = Comparison.getInstance().withSavingsSummary(summarySavings);
        log.warn("Comparison prepared");

        printingService.printSavingsInformation(savingsData, mortgageData);
        printingService.printSavingsSummary(summarySavings);
        printingService.printSavings(savings);


        printingService.printComparison(comparison, mortgageData);
    }

    public List<Savings> calculateSavings(SavingsData savingsData, MortgageData mortgageData) {
        List<Savings> savings = new ArrayList<>();

        BigDecimal savingsNumber = BigDecimal.ONE;

        Savings firstSaving = savingsAmountCalculationService.calculateSaving(savingsData, savingsNumber, mortgageData);
        log.debug("FirstSaving: [{}]",firstSaving);

        savings.add(firstSaving);

        Savings previousSaving = firstSaving;

        BigDecimal numberOfYears = mortgageData.monthsDuration().divide(CalculatingMethods.YEAR, 2, RoundingMode.HALF_UP);

        for (savingsNumber = savingsNumber.add(BigDecimal.ONE);
             savingsNumber.compareTo(numberOfYears) <= 0;
             savingsNumber = savingsNumber.add(BigDecimal.ONE)) {

            Savings nextSaving = savingsAmountCalculationService
                .calculateSaving(previousSaving, savingsNumber, mortgageData, savingsData);
            log.debug("NextSaving: [{}]",nextSaving);
            savings.add(nextSaving);
            previousSaving = nextSaving;

        }
        return savings;
    }


}
