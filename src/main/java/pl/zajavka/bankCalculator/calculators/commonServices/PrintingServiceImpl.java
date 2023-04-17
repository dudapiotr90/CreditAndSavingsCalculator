package pl.zajavka.bankCalculator.calculators.commonServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsSummary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
@Slf4j
@Service
public class PrintingServiceImpl implements PrintingService {

    private static final String SEPARATOR = createSeparator('\u2501', 200);

    private static final Path RESULT_DIRECTORY = Paths.get("./results");
    private static final Path RESULT_FILE_PATH = Paths.get(RESULT_DIRECTORY+"/bankCalculatorResult");

    @SuppressWarnings("SameParameterValue")
    private static String createSeparator(char sign, int length) {
        return String.valueOf(sign).repeat(Math.max(0, length))+System.lineSeparator();
    }

    private static BigDecimal getOverpaymentProfit(MortgageSummary withoutOverpaymentSummary, MortgageSummary summary) {
        return withoutOverpaymentSummary.totalLosses().subtract(summary.totalLosses()
            .setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Override
    public void printIntroInformation(MortgageData mortgageData) {
        String introInformation = MORTGAGE_INFORMATION.formatted(
            mortgageData.amount(),
            mortgageData.monthsDuration(),
            mortgageData.interestDisplay()
        );

        if (Optional.ofNullable(mortgageData.overpaymentSchema())
            .map(schema -> schema.size() > 0)
            .orElse(false)) {

            String overpaymentInformation = OVERPAYMENT_INFORMATION.formatted(
                OVERPAYMENT_REDUCE_PERIOD.equals(mortgageData.overpaymentReduceWay()) ?
                    OVERPAYMENT_REDUCE_PERIOD :
                    OVERPAYMENT_REDUCE_RATE,
                paymentSchedule(mortgageData.overpaymentSchema())
            );
            introInformation += overpaymentInformation;
        }

        addToReport(introInformation);
    }

    private String paymentSchedule(Map<Integer, BigDecimal> paymentSchedule) {
        return paymentSchedule.entrySet().stream()
            .reduce(
                new StringBuilder(),
                (previous, next) -> previous.append(String.format(PAYMENT, next.getKey(), next.getValue())),
                StringBuilder::append)
            .toString();
    }

    @Override
    public void printRatesSchedule(List<Rate> rates, MortgageData mortgageData) {
        if (!mortgageData.mortgagePrintPayoffsSchedule()) {
            return;
        }
        rates.stream()
            .filter(rate -> rate.rateNumber().remainder(
                BigDecimal.valueOf(mortgageData.mortgageRateNumberToPrint())).equals(BigDecimal.ZERO))
            .forEach(rate -> {
                addToReport(formatRateLine(rate));
                if (CalculatingMethods.YEAR.equals(rate.mortgageTimePoint().month())) {
                    addToReport(SEPARATOR);
                }
            });
    }

    private String formatRateLine(Rate rate) {
        return String.format(RATES_FORMAT,
            RATE_INFORMATION.get(0), rate.rateNumber(),
            RATE_INFORMATION.get(1), rate.mortgageTimePoint().date(),
            RATE_INFORMATION.get(2), rate.mortgageTimePoint().year(),
            RATE_INFORMATION.get(3), rate.mortgageTimePoint().month(),
            RATE_INFORMATION.get(4), rate.rateAmounts().rateAmount().setScale(2, RoundingMode.HALF_UP),
            RATE_INFORMATION.get(5), rate.rateAmounts().interest().setScale(2, RoundingMode.HALF_UP),
            RATE_INFORMATION.get(6), rate.rateAmounts().capitalAmount().setScale(2, RoundingMode.HALF_UP),
            RATE_INFORMATION.get(7), rate.rateAmounts().overpayment().amount().setScale(2, RoundingMode.HALF_UP),
            RATE_INFORMATION.get(8), rate.mortgageResidual().amount().setScale(2, RoundingMode.HALF_UP),
            RATE_INFORMATION.get(9), rate.mortgageResidual().duration()
        );
    }

    @Override
    public void printMortgageSummary(MortgageSummary summary) {
        String summaryInformation = MORTGAGE_SUMMARY.formatted(
            summary.interestSum().setScale(2, RoundingMode.HALF_UP),
            summary.overpaymentProvisions().setScale(2, RoundingMode.HALF_UP),
            summary.totalLosses().setScale(2, RoundingMode.HALF_UP),
            summary.totalCapital().setScale(2, RoundingMode.HALF_UP)

        );

        addToReport(summaryInformation);

    }

    @Override
    public void printOverpaymentProfit(MortgageSummary withoutOverpaymentSummary, MortgageSummary summary) {
        if (withoutOverpaymentSummary.totalLosses().compareTo(summary.totalLosses()) == 0) {
            return;
        }
        String overpaymentProfit = OVERPAYMENT_PROFIT.formatted(
            withoutOverpaymentSummary.totalLosses().setScale(2, RoundingMode.HALF_UP),
            getOverpaymentProfit(withoutOverpaymentSummary, summary));

        addToReport(overpaymentProfit);
    }

    @Override
    public void printSavingsInformation(SavingsData savingsData, MortgageData mortgageData) {
        if (Optional.ofNullable(savingsData.depositSchema())
            .map(schema -> schema.size() == 0)
            .orElse(false)) {
            addToReport(EMPTY_SAVINGS);
            return;
        }
        String savingsInformation = SAVINGS_INFORMATION.formatted(
            paymentSchedule(Objects.requireNonNull(savingsData.depositSchema())),
            savingsData.depositInterestDisplay(),
            savingsData.interestCapitalization().getValue()
        );
        addToReport(savingsInformation);

    }

    @Override
    public void printSavingsSummary(SavingsSummary summary) {
        String savingsSummary = SAVINGS_SUMMARY.formatted(
            summary.savingsSum(),
            summary.earnings()
        );
        addToReport(savingsSummary);
    }

    @Override
    public void printSavings(List<Savings> savings) {
        savings.forEach(saving -> addToReport(formatSavingLine(saving)));
        addToReport(SEPARATOR);
    }

    private String formatSavingLine(Savings saving) {
        return String.format(SAVINGS_FORMAT,
            SAVING_INFORMATION.get(0), saving.savingsTimePoint().year(),
            SAVING_INFORMATION.get(1), saving.savingsAmount().savingAmount(),
            SAVING_INFORMATION.get(2), saving.savingsAmount().interestAmount()
        );
    }

    @Override
    public void printComparison(Comparison comparison, MortgageData mortgageData) {
        if (!mortgageData.compareToSavings()) {
            return;
        }

        BigDecimal savingsProfit = comparison.savingsSummary().earnings();
        BigDecimal overpaymentProfit = getOverpaymentProfit(
            comparison.withoutOverpaymentMortgageSummary(),
            comparison.mortgageSummary()
        );
        BigDecimal comparedProfits = savingsProfit.subtract(overpaymentProfit);

        if (comparedProfits.compareTo(BigDecimal.ZERO) < 0) {
            comparedProfits = comparedProfits.abs();
            String compared = String.format(BETTER_TO_OVERPAY_MORTGAGE, comparedProfits);
            addToReport(compared);
        } else if (comparedProfits.compareTo(BigDecimal.ZERO) == 0) {
            String compared = String.format(NO_DIFFERENCE);
            addToReport(compared);
        } else if (comparedProfits.compareTo(BigDecimal.ZERO) > 0) {
            String compared = String.format(BETTER_TO_SAVE,comparedProfits);
            addToReport(compared);
        }

    }

    public void addToReport(String message) {
        try {
            if (!Files.exists(RESULT_FILE_PATH)) {
                Files.createDirectories(RESULT_DIRECTORY);
                Files.createFile(RESULT_FILE_PATH);
            }
            Files.writeString(RESULT_FILE_PATH, message, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.printf("Error generating results: [%s]",e.getMessage());
            log.error("Error generating results: [{}]",e.getMessage());
        }
    }

}
