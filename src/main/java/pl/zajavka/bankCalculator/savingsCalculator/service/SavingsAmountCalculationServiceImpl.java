package pl.zajavka.bankCalculator.savingsCalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsAmount;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsTimePoint;

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
        SavingsData savingsData, BigDecimal savingsNumber, InputData inputData
    ) {
        SavingsTimePoint timePointSavings = savingsTimeCalculationService
            .calculate(savingsData, savingsNumber, inputData);
        SavingsAmount savingsAmount = calculateAmount(savingsData, timePointSavings);
        return new Savings(savingsAmount, timePointSavings, savingsNumber);
    }

    @Override
    public Savings calculateSaving(
        Savings previousSaving, BigDecimal savingsNumber, InputData inputData, SavingsData savingsData
    ) {
        SavingsTimePoint timePointSavings = savingsTimeCalculationService
            .calculate(savingsData, savingsNumber, previousSaving);
        SavingsAmount savingsAmount = calculateAmount(previousSaving, savingsData, timePointSavings);
        return new Savings(savingsAmount, timePointSavings, savingsNumber);
    }

    private SavingsAmount calculateAmount(SavingsData savingsData, SavingsTimePoint timePointSavings) {
        BigDecimal interestAmount;
        BigDecimal savingAmount;
        switch (savingsData.interestCapitalization()) {
            case AFTER_YEAR -> {
                interestAmount = interestCalculationService.calculateInterestByYear(savingsData, timePointSavings);
                log.info("InterestAmount: [{}]",interestAmount);
                savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, timePointSavings);
                log.info("SavingAmount: [{}]",savingAmount);
                return new SavingsAmount(savingAmount, interestAmount);
            }
            case AFTER_QUARTER -> {
                interestAmount = interestCalculationService.calculateInterestByQuarter(savingsData, timePointSavings);
                log.info("InterestAmount: [{}]",interestAmount);
                savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, timePointSavings);
                log.info("SavingAmount: [{}]",savingAmount);
                return new SavingsAmount(savingAmount, interestAmount);
            }
            case AFTER_MONTH -> {
                interestAmount = interestCalculationService.calculateInterestByMonth(savingsData, timePointSavings);
                log.info("InterestAmount: [{}]",interestAmount);
                savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, timePointSavings);
                log.info("SavingAmount: [{}]",savingAmount);
                return new SavingsAmount(savingAmount, interestAmount);
            }
            default -> {
                log.error("SavingsData.interestCapitalization(): [{}]",savingsData.interestCapitalization());
                throw new RuntimeException();
            }
        }
    }

    private SavingsAmount calculateAmount(
        Savings previousSaving,
        SavingsData savingsData,
        SavingsTimePoint timePointSavings
    ) {
        BigDecimal interestAmount;
        BigDecimal savingAmount;
        switch (savingsData.interestCapitalization()) {
            case AFTER_YEAR -> {
                interestAmount = interestCalculationService.calculateInterestByYear(savingsData, previousSaving, timePointSavings);
                log.info("InterestAmount: [{}]",interestAmount);
                savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, previousSaving, timePointSavings);
                log.info("SavingAmount: [{}]",savingAmount);
                return new SavingsAmount(savingAmount, interestAmount);
            }
            case AFTER_QUARTER -> {
                interestAmount = interestCalculationService.calculateInterestByQuarter(savingsData, previousSaving, timePointSavings);
                log.info("InterestAmount: [{}]", interestAmount);
                savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, previousSaving, timePointSavings);
                log.info("SavingAmount: [{}]", savingAmount);
                return new SavingsAmount(savingAmount, interestAmount);
            }
            case AFTER_MONTH -> {
                interestAmount = interestCalculationService.calculateInterestByMonth(savingsData, previousSaving, timePointSavings);
                log.info("InterestAmount: [{}]",interestAmount);
                savingAmount = amountCalculationService.calculateSavingAmount(savingsData, interestAmount, previousSaving, timePointSavings);
                log.info("SavingAmount: [{}]",savingAmount);
                return new SavingsAmount(savingAmount, interestAmount);
            }
            default -> {
                log.error("SavingsData.interestCapitalization(): [{}]",savingsData.interestCapitalization());
                throw new RuntimeException();
            }
        }
    }
}