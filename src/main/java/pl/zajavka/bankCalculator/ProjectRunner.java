package pl.zajavka.bankCalculator;

import lombok.extern.slf4j.Slf4j;
import pl.zajavka.bankCalculator.creditCalculator.MortgageDependenciesCreator;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateTypes;
import pl.zajavka.bankCalculator.creditCalculator.service.MortgageCalculationService;
import pl.zajavka.bankCalculator.globalServices.InputDataService;
import pl.zajavka.bankCalculator.savingsCalculator.SavingsDependenciesCreator;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.service.SavingsCalculationService;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
public class ProjectRunner {

    public static void main(String[] args) {

        InputData defaultMortgageData;
        SavingsData defaultSavingsData;
        try {
            defaultMortgageData = new InputDataService().readMortgageDataFile();
            defaultSavingsData = new InputDataService().readSavingsDataFile();


        } catch (Exception e) {
            System.err.println("Error loading inputData.csv , stopping program! Error: " + e.getMessage());
            log.error("Error loading inputData.csv , stopping program! Error: [{}]", e.getMessage());
            return;
        }

        InputData customInputData = defaultMortgageData
            .withAmount(new BigDecimal("150000"))
            .withMonthsDuration(BigDecimal.valueOf(180))
            .withRateType(RateTypes.DECREASING)
            .withOverpaymentReduceWay(Overpayment.REDUCE_PERIOD)
//                .withOverpaymentSchema(Collections.emptyMap());
            .withOverpaymentSchema(Map.of(
                1, BigDecimal.valueOf(10000),
                6, BigDecimal.valueOf(12000),
                11, BigDecimal.valueOf(13000),
                13, BigDecimal.valueOf(16000)));


        CalculatorsCreator.getMortgageInstance().calculate(defaultMortgageData);
        CalculatorsCreator.getSavingsInstance().calculate(defaultSavingsData, defaultMortgageData);
    }

    static class CalculatorsCreator {

        public static MortgageCalculationService getMortgageInstance() {
            return MortgageDependenciesCreator.getInstance();
        }

        public static SavingsCalculationService getSavingsInstance() {
            return SavingsDependenciesCreator.getInstance();
        }
    }
}
