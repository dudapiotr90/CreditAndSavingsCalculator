package pl.zajavka.bankCalculator.savingsCalculator.service;

import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsTimePoint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static pl.zajavka.bankCalculator.globalServices.CalculatingMethods.YEAR;

@Service
public class SavingsTimeCalculationServiceImpl implements SavingsTimeCalculationService {


    @Override
    public SavingsTimePoint calculate(SavingsData savingsData, BigDecimal savingsNumber, InputData inputData) {
        if (!BigDecimal.ONE.equals(savingsNumber)) {
            throw new RuntimeException("This method only accepts rateNumber equal to ONE");
        }
        LocalDate date = inputData.repaymentStartDate();
        BigDecimal year = calculateYear(savingsNumber);
        List<Integer> months = calculateMonths();

        return new SavingsTimePoint(date, year, months);
    }

    @Override
    public SavingsTimePoint calculate(SavingsData savingsData, BigDecimal savingsNumber, Savings previousSaving) {
        LocalDate date = previousSaving.savingsTimePoint().date().plus(1, ChronoUnit.MONTHS);
        BigDecimal year = calculateYear(savingsNumber);
        List<Integer> months = calculateMonths(previousSaving);

        return new SavingsTimePoint(date, year, months);
    }

    private List<Integer> calculateMonths() {
        int adder = 1;
        List<Integer> months = new ArrayList<>();
        for (int i = 0; i < YEAR.intValue(); i++) {
            months.add(i, i + adder);
        }
        return months;
    }

    private List<Integer> calculateMonths(Savings previousSaving) {
        int adder = 1;
        List<Integer> months = new ArrayList<>(previousSaving.savingsTimePoint().months());

        for (int i = 0; i < months.size(); i++) {
            months.set(i, months.get(months.size() - 1) + adder + i);

        }
        return months;
    }

    private BigDecimal calculateYear(BigDecimal savingsNumber) {
        return savingsNumber.max(BigDecimal.ONE);
    }


}
