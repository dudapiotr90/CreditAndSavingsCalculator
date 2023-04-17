package pl.zajavka.bankCalculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.zajavka.bankCalculator.creditCalculator.MortgageDependenciesCreator;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateTypes;
import pl.zajavka.bankCalculator.creditCalculator.service.MortgageCalculationService;
import pl.zajavka.bankCalculator.globalServices.InputDataRepository;
import pl.zajavka.bankCalculator.savingsCalculator.SavingsDependenciesCreator;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.service.SavingsCalculationService;
import pl.zajavka.bankCalculator.configuration.CalculatorConfiguration;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ProjectRunner {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(CalculatorConfiguration.class);

        InputDataRepository inputDataRepository = context.getBean(InputDataRepository.class);

        Optional<InputData> defaultMortgageData = inputDataRepository.readMortgageDataFile();
        Optional<SavingsData> defaultSavingsData = inputDataRepository.readSavingsDataFile();

        if (defaultMortgageData.isEmpty()) {
            return;
        }

//        InputData defaultMortgageData;
//        SavingsData defaultSavingsData;
//        try {
//            defaultMortgageData = new InputDataRepository().readMortgageDataFile();
//            defaultSavingsData = new InputDataRepository().readSavingsDataFile();
//
//
//        } catch (Exception e) {
//            System.err.println("Error loading inputData.csv , stopping program! Error: " + e.getMessage());
//            log.error("Error loading inputData.csv , stopping program! Error: [{}]", e.getMessage());
//            return;
//        }

//        InputData customInputData = defaultMortgageData
//            .withAmount(new BigDecimal("150000"))
//            .withMonthsDuration(BigDecimal.valueOf(180))
//            .withRateType(RateTypes.DECREASING)
//            .withOverpaymentReduceWay(Overpayment.REDUCE_PERIOD)
////                .withOverpaymentSchema(Collections.emptyMap());
//            .withOverpaymentSchema(Map.of(
//                1, BigDecimal.valueOf(10000),
//                6, BigDecimal.valueOf(12000),
//                11, BigDecimal.valueOf(13000),
//                13, BigDecimal.valueOf(16000)));
        MortgageCalculationService mortgageCalculationService = context.getBean(MortgageCalculationService.class);
        mortgageCalculationService.calculate(defaultMortgageData.get());

        if (defaultSavingsData.isEmpty()) {
            return;
        }
        SavingsCalculationService savingsCalculationService = context.getBean(SavingsCalculationService.class);
        savingsCalculationService.calculate(defaultSavingsData.get(),defaultMortgageData.get());
//        CalculatorsCreator.getMortgageInstance().calculate(defaultMortgageData);
//        CalculatorsCreator.getSavingsInstance().calculate(defaultSavingsData, defaultMortgageData);
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
