package pl.zajavka.bankCalculator.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.RateCalculationService;
import pl.zajavka.bankCalculator.configuration.BankCalculatorConfiguration;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

@SpringJUnitConfig(classes = {BankCalculatorConfiguration.class})
public class RateCalculationServiceIT {

    @Autowired
    private RateCalculationService rateCalculationService;

    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(rateCalculationService);
    }

    @Test
    @DisplayName("Test rate calculation")
    void test() {
        // given
        final var mortgageData = TestMortgageData.someMortgageData();

        // when
        final var result = rateCalculationService.calculate(mortgageData);

        // then
//        Assertions.assertEquals(180, result.size());
    }
}
