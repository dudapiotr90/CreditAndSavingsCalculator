package pl.zajavka.bankCalculator.globalServices;

import lombok.With;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsSummary;

@With
public class Comparison {

    private static Comparison instance;
    private MortgageSummary mortgageSummary;

    private MortgageSummary withoutOverpaymentMortgageSummary;
    private SavingsSummary savingsSummary;

    private Comparison(
        MortgageSummary mortgageSummary,
        MortgageSummary withoutOverpaymentMortgageSummary,
        SavingsSummary savingsSummary
    ) {
        this.mortgageSummary = mortgageSummary;
        this.withoutOverpaymentMortgageSummary = withoutOverpaymentMortgageSummary;
        this.savingsSummary = savingsSummary;
    }

    public static Comparison getInstance(
        MortgageSummary mortgageSummary,
        MortgageSummary withoutOverpaymentMortgageSummary,
        SavingsSummary savingsSummary
    ) {
        instance = new Comparison(mortgageSummary,withoutOverpaymentMortgageSummary, savingsSummary);
        return instance;
    }

    public static Comparison getInstance() {
        return instance;
    }

    public MortgageSummary mortgageSummary() {
        return mortgageSummary;
    }

    public SavingsSummary savingsSummary() {
        return savingsSummary;
    }

    public MortgageSummary withoutOverpaymentMortgageSummary() {
        return withoutOverpaymentMortgageSummary;
    }
}
