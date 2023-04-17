package pl.zajavka.bankCalculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.zajavka.bankCalculator.calculators.BankCalculatorService;
import pl.zajavka.bankCalculator.configuration.BankCalculatorConfiguration;

@Slf4j
public class ProjectRunner {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(BankCalculatorConfiguration.class);
        BankCalculatorService bankCalculatorService = context.getBean(BankCalculatorService.class);
        bankCalculatorService.calculate();

    }
}
