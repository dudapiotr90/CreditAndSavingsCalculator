package pl.zajavka.bankCalculator.creditCalculator;

import pl.zajavka.bankCalculator.creditCalculator.service.*;
import pl.zajavka.bankCalculator.globalServices.PrintingServiceImpl;

import java.util.Objects;

public class MortgageDependenciesCreator {

    private static MortgageCalculationService instance;

    public static MortgageCalculationService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new MortgageCalculationServiceImpl(
                new PrintingServiceImpl(),
                new RateCalculationServiceImpl(
                    new TimePointCalculationServiceImpl(),
                    new ResidualCalculationServiceImpl(),
                    new OverpaymentCalculationServiceImpl(),
                    new RateAmountsCalculationServiceImpl(
                        new ConstantAmountsCalculationServiceImpl(),
                        new DecreasingAmountsCalculationServiceImpl()
                    ),
                    new ReferenceCalculationServiceImpl()),
                SummaryServiceFactory.create()
            );
        }
        return instance;
    }
}
