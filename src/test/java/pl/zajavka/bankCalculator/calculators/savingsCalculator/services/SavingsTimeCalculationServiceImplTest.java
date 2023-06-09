package pl.zajavka.bankCalculator.calculators.savingsCalculator.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.Savings;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsTimePoint;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;
import pl.zajavka.bankCalculator.fixtures.TestSavingsData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@ExtendWith(MockitoExtension.class)
class SavingsTimeCalculationServiceImplTest {

    @InjectMocks
    SavingsTimeCalculationServiceImpl savingsTimeCalculationService;

    @Test
    void shouldCalculateSavingsTimePointForFirstDepositCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        BigDecimal savingsNumber = BigDecimal.ONE;
        MortgageData mortgageData = TestMortgageData.someMortgageData()
            .withRepaymentStartDate(LocalDate.of(2017,6,8));

        SavingsTimePoint expected = TestSavingsData.someSavingsTimePoint()
            .withYear(BigDecimal.ONE);
        String expectedExceptionMessage = "This method only accepts rateNumber equal to ONE";


        //when
        SavingsTimePoint result = savingsTimeCalculationService.calculate(savingsData, savingsNumber, mortgageData);
        Throwable exception = Assertions.assertThrows(RuntimeException.class,
            () -> savingsTimeCalculationService.calculate(savingsData,BigDecimal.TEN, mortgageData));
        //then
        Assertions.assertEquals(expected,result);
        Assertions.assertEquals(expectedExceptionMessage,exception.getMessage());

    }

    @Test
    void shouldCalculateSavingsTimePointForOtherDepositsCorrectly() {
        //given
        SavingsData savingsData = TestSavingsData.someSavingsData();
        BigDecimal savingsNumber = BigDecimal.valueOf(2);
        Savings savings = TestSavingsData.someSavings();

        SavingsTimePoint expected = TestSavingsData.someSavingsTimePoint()
            .withMonths(List.of(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24))
            .withYear(BigDecimal.valueOf(2))
            .withDate(LocalDate.of(2017,7,8));

        //when
        SavingsTimePoint result = savingsTimeCalculationService.calculate(savingsData, savingsNumber, savings);

        //then
        Assertions.assertEquals(expected,result);

    }
}