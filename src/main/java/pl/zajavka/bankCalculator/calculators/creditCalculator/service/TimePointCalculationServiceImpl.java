package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.CalculatingMethods;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageTimePoint;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class TimePointCalculationServiceImpl implements TimePointCalculationService {


    @Override
    public MortgageTimePoint calculate(BigDecimal rateNumber, MortgageData mortgageData) {
        if (!BigDecimal.ONE.equals(rateNumber)) {
            throw new RuntimeException("This method only accepts rateNumber equal to ONE");
        }
        LocalDate date = mortgageData.repaymentStartDate();
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
        return rateNumber.divide(CalculatingMethods.YEAR, RoundingMode.UP).max(BigDecimal.ONE);
    }

    private BigDecimal calculateMonth(BigDecimal rateNumber) {
        return BigDecimal.ZERO.equals(rateNumber.remainder(CalculatingMethods.YEAR)) ? CalculatingMethods.YEAR : rateNumber.remainder(CalculatingMethods.YEAR);
    }
}
