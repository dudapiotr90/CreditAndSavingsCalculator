package pl.zajavka.bankCalculator.globalServices;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsSummary;

import java.util.List;

public interface PrintingService {

    String MORTGAGE_INFORMATION = """
        KWOTA KREDYTU: %s ZŁ
        OKRES KREDYTOWANIA: %s MIESIĘCY
        ODSETKI: %s %%
        """;

    String OVERPAYMENT_INFORMATION = """
        %s
        SCHEMAT DOKONYWANIA NADPŁAT:
        %s
        """;

    String MORTGAGE_SUMMARY = """
        SUMA ODSETEK: %s ZŁ
        PROWIZJA ZA NADPŁATY: %s ZŁ
        SUMA STRAT: %s ZŁ
        SUMA SPŁAT: %s ZŁ%n
        """;

    String OVERPAYMENT_PROFIT = """
        BEZ NADPŁACANIA SUMA STRAT WYNOSI: %s ZŁ
        ZYSK Z NADPLAT: %s ZŁ%n
        """;
    String RATES_FORMAT =
        "%1s %4s  |" +
            "%7s %4s  |" +
            "%6s %3s  |" +
            "%10s %3s  |" +
            "%7s %9s  |" +
            "%10s %8s  |" +
            "%10s %8s  |" +
            "%11s %9s  |" +
            "%10s %10s  |" +
            "%7s %4s %n";

    List<String> RATE_INFORMATION = List.of(
        "NR:",
        "DATA:",
        "ROK:",
        "MIESIAC:",
        "RATA:",
        "ODSETKI:",
        "KAPITAL:",
        "NADPLATA:",
        "PKWOTA: ",
        "PMSC:"
    );

    String PAYMENT = "MIESIĄC: %s, KWOTA: %s ZŁ%n";
    String OVERPAYMENT_REDUCE_RATE = "NADPŁATA, ZMNIEJSZENIE RATY:";
    String OVERPAYMENT_REDUCE_PERIOD = "NADPŁATA, SKRÓCENIE OKRESU";
    String EMPTY_SAVINGS = "BRAK WPŁACONYCH ŚRODKÓW";
    String SAVINGS_INFORMATION = """
        
        SCHEMAT DOKONYWANIA WPŁAT:
        %sOPROCENTOWANIE: %s %%
        KAPITALIZACJA ODSETEK: %s%n
        """;

    String SAVINGS_SUMMARY = """
        SUMA OSZCZĘDNOŚCI: %s ZŁ
        ZYSK: %s ZŁ%n
        """;

    String SAVINGS_FORMAT = "%2s %3s   |" +
        "%24s %12s   |" +
        "%21s %11s %n";

    List<String> SAVING_INFORMATION = List.of(
        "ROK OSZCZĘDZANIA:",
        "BIEŻĄCE OSZCZĘDNOŚCI:",
        "NALICZONE ODSETKI:"
    );

    String BETTER_TO_SAVE = "KORZYSTNIEJ JEST WPŁACIĆ NA LOKATĘ, ZYSKUJEMY: %s ZŁ%n";
    String BETTER_TO_OVERPAY_MORTGAGE = "KORZYSTNIEJ JEST NADPŁACAĆ KRETYD, ZYSKUJEMY: %s ZŁ%n";
    String NO_DIFFERENCE = "NADPŁACANIE KREDYTU JEST RÓWNIE DOBRE JAK WPŁACA NA LOKATĘ%n";

    void printIntroInformation(InputData inputData);

    void printRatesSchedule(List<Rate> rates, InputData inputData);

    void printMortgageSummary(MortgageSummary summary);

    void printOverpaymentProfit(MortgageSummary withoutOverpaymentSummary, MortgageSummary summary);

    void printSavingsInformation(SavingsData savingsData, InputData inputData);

    void printSavingsSummary(SavingsSummary summary);

    void printSavings(List<Savings> savings);


    void printComparison(Comparison comparison, InputData inputData);
}
