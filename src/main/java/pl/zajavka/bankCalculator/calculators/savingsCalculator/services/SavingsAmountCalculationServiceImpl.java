package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsAmount;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingsAmountCalculationServiceImpl implements SavingsAmountCalculationService {


    private final SavingsTimeCalculationService savingsTimeCalculationService;
    private final InterestCalculationService interestCalculationService;
    private final AmountCalculationService amountCalculationService;

    @Override
    public Savings calculateSaving(
        SavingsData savingsData, BigDecimal savingsNumber, MortgageData mortgageData
    ) {
        if (!BigDecimal.ONE.equals(savingsNumber)) {
            throw new RuntimeException("This method only accepts savingsNumber equal to ONE");
        }
        SavingsTimePoint timePointSavings = savingsTimeCalculationService
            .calculate(savingsData, savingsNumber, mortgageData);
        SavingsAmount savingsAmount = calculateAmount(savingsData, timePointSavings);
        return new Savings(savingsAmount, timePointSavings, savingsNumber);
    }

    @Override
    public Savings calculateSaving(
        Savings previousSaving, BigDecimal savingsNumber, MortgageData mortgageData, SavingsData savingsData
    ) {
        SavingsTimePoint timePointSavings = savingsTimeCalculationService
            .calculate(savingsData, savingsNumber, previousSaving);
        SavingsAmount savingsAmount = calculateAmount(previousSaving, savingsData, timePointSavings);
        return new Savings(savingsAmount, timePointSavings, savingsNumber);
    }

    private SavingsAmount calculateAmount(SavingsData savingsData, SavingsTimePoint timePointSavings) {

        return switch (savingsData.interestCapitalization()) {
            case AFTER_YEAR -> calculateSavingsAmountAfterYear(savingsData, timePointSavings);
            case AFTER_QUARTER -> calculateSavingsAmountAfterQuarter(savingsData, timePointSavings);
            case AFTER_MONTH -> calculateSavingsAmountAfterMonth(savingsData, timePointSavings);
        };
    }

    private SavingsAmount calculateAmount(
        Savings previousSaving,
        SavingsData savingsData,
        SavingsTimePoint timePointSavings
    ) {
        return switch (savingsData.interestCapitalization()) {
            case AFTER_YEAR -> calculateSavingsAmountAfterYear(previousSaving, savingsData, timePointSavings);
            case AFTER_QUARTER -> calculateSavingsAmountAfterQuarter(previousSaving, savingsData, timePointSavings);
            case AFTER_MONTH -> calculateSavingsAmountAfterMonth(previousSaving, savingsData, timePointSavings);
        };
    }

    private SavingsAmount calculateSavingsAmountAfterMonth(SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal savingAmount;
        BigDecimal interestAmount;
        interestAmount = interestCalculationService.calculateInterestByMonth(savingsData, timePointSavings);
        log.info("InterestAmount: [{}]", interestAmount);
        savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, timePointSavings);
        log.info("SavingAmount: [{}]", savingAmount);
        return new SavingsAmount(savingAmount, interestAmount);
    }

    private SavingsAmount calculateSavingsAmountAfterMonth(Savings previousSaving, SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal interestAmount;
        BigDecimal savingAmount;
        interestAmount = interestCalculationService.calculateInterestByMonth(savingsData, previousSaving, timePointSavings);
        log.info("InterestAmount: [{}]", interestAmount);
        savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, previousSaving, timePointSavings);
        log.info("SavingAmount: [{}]", savingAmount);
        return new SavingsAmount(savingAmount, interestAmount);
    }

    private SavingsAmount calculateSavingsAmountAfterQuarter(SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal savingAmount;
        BigDecimal interestAmount;
        interestAmount = interestCalculationService.calculateInterestByQuarter(savingsData, timePointSavings);
        log.info("InterestAmount: [{}]", interestAmount);
        savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, timePointSavings);
        log.info("SavingAmount: [{}]", savingAmount);
        return new SavingsAmount(savingAmount, interestAmount);
    }

    private SavingsAmount calculateSavingsAmountAfterQuarter(Savings previousSaving, SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal savingAmount;
        BigDecimal interestAmount;
        interestAmount = interestCalculationService.calculateInterestByQuarter(savingsData, previousSaving, timePointSavings);
        log.info("InterestAmount: [{}]", interestAmount);
        savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, previousSaving, timePointSavings);
        log.info("SavingAmount: [{}]", savingAmount);
        return new SavingsAmount(savingAmount, interestAmount);
    }

    private SavingsAmount calculateSavingsAmountAfterYear(SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal savingAmount;
        BigDecimal interestAmount;
        interestAmount = interestCalculationService.calculateInterestByYear(savingsData, timePointSavings);
        log.info("InterestAmount: [{}]", interestAmount);
        savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, timePointSavings);
        log.info("SavingAmount: [{}]", savingAmount);
        return new SavingsAmount(savingAmount, interestAmount);
    }

    private SavingsAmount calculateSavingsAmountAfterYear(Savings previousSaving, SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal interestAmount;
        BigDecimal savingAmount;
        interestAmount = interestCalculationService.calculateInterestByYear(savingsData, previousSaving, timePointSavings);
        log.info("InterestAmount: [{}]", interestAmount);
        savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, previousSaving, timePointSavings);
        log.info("SavingAmount: [{}]", savingAmount);
        return new SavingsAmount(savingAmount, interestAmount);
    }
}
