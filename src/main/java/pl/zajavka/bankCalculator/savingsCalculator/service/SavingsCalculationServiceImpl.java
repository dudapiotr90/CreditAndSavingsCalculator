package pl.zajavka.bankCalculator.savingsCalculator.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.globalServices.Comparison;
import pl.zajavka.bankCalculator.globalServices.PrintingService;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsSummary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.YEAR;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingsCalculationServiceImpl implements SavingsCalculationService {

    private final PrintingService printingService;
    private final SavingsAmountCalculationService savingsAmountCalculationService;
    private final SavingsSummaryService savingsSummaryService;


    @Override
    public void calculate(SavingsData savingsData, InputData inputData) {
        log.info("SavingsData: [{}]", savingsData);

        List<Savings> savings = calculateSavings(savingsData, inputData);
        savings.forEach(saving -> log.debug("Saving: [{}]", saving));

        SavingsSummary summarySavings = savingsSummaryService.calculateSavings(savings);

        Comparison comparison = Comparison.getInstance().withSavingsSummary(summarySavings);
        log.warn("Comparison prepared");

        printingService.printSavingsInformation(savingsData, inputData);
        printingService.printSavingsSummary(summarySavings);
        printingService.printSavings(savings);


        printingService.printComparison(comparison,inputData);
    }

    public List<Savings> calculateSavings(SavingsData savingsData, InputData inputData) {
        List<Savings> savings = new ArrayList<>();

        BigDecimal savingsNumber = BigDecimal.ONE;

        Savings firstSaving = savingsAmountCalculationService.calculateSaving(savingsData, savingsNumber, inputData);
        log.debug("FirstSaving: [{}]",firstSaving);

        savings.add(firstSaving);

        Savings previousSaving = firstSaving;

        BigDecimal numberOfYears = inputData.monthsDuration().divide(YEAR, 2, RoundingMode.HALF_UP);

        for (savingsNumber = savingsNumber.add(BigDecimal.ONE);
             savingsNumber.compareTo(numberOfYears) <= 0;
             savingsNumber = savingsNumber.add(BigDecimal.ONE)) {

            Savings nextSaving = savingsAmountCalculationService
                .calculateSaving(previousSaving, savingsNumber, inputData, savingsData);
            log.debug("NextSaving: [{}]",nextSaving);
            savings.add(nextSaving);
            previousSaving = nextSaving;

        }
        return savings;
    }


}
