package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageTimePoint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.YEAR;

public class TimePointCalculationServiceImpl implements TimePointCalculationService {


    @Override
    public MortgageTimePoint calculate(BigDecimal rateNumber, InputData inputData) {
        if (!BigDecimal.ONE.equals(rateNumber)) {
            throw new RuntimeException("This method only accepts rateNumber equal to ONE");
        }
        LocalDate date = inputData.repaymentStartDate();
        BigDecimal year = calculateYear(rateNumber);
        BigDecimal month = calculateMonth(rateNumber);
        return new MortgageTimePoint(date, year, month);
    }

    @Override
    public MortgageTimePoint calculate(BigDecimal rateNumber, Rate previousRate) {
        BigDecimal year = calculateYear(rateNumber);
        BigDecimal month = calculateMonth(rateNumber);
        LocalDate date = previousRate.mortgageTimePoint().date().plus(1, ChronoUnit.MONTHS);
        return new MortgageTimePoint(date, year, month);
    }

    private BigDecimal calculateYear(BigDecimal rateNumber) {
        return rateNumber.divide(YEAR, RoundingMode.UP).max(BigDecimal.ONE);
    }

    private BigDecimal calculateMonth(BigDecimal rateNumber) {
        return BigDecimal.ZERO.equals(rateNumber.remainder(YEAR)) ? YEAR : rateNumber.remainder(YEAR);
    }
}
