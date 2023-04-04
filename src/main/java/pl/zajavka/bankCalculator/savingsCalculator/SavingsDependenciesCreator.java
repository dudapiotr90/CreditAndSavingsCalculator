package pl.zajavka.bankCalculator.savingsCalculator;

import pl.zajavka.bankCalculator.globalServices.PrintingServiceImpl;
import pl.zajavka.bankCalculator.savingsCalculator.service.*;

import java.util.Objects;

public class SavingsDependenciesCreator {

    private static SavingsCalculationService instance;


    public static SavingsCalculationService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SavingsCalculationServiceImpl(
                new PrintingServiceImpl(),
                new SavingsAmountCalculationServiceImpl(
                    new SavingsTimeCalculationServiceImpl(),
                    new InterestCalculationServiceImpl(),
                    new AmountCalculationServiceImpl()),
                SavingsSummaryFactory.createSavings()
            );
        }
        return instance;
    }
}
