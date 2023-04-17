package pl.zajavka.bankCalculator.calculators.commonServices;

import lombok.With;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageSummary;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsSummary;

@With
public class Comparison {

    private static Comparison instance;
    private MortgageSummary mortgageSummary;

    private MortgageSummary withoutOverpaymentMortgageSummary;
    private SavingsSummary savingsSummary;

//    @Autowired
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
