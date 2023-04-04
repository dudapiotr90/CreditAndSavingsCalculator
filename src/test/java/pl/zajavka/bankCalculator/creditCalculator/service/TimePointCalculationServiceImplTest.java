package pl.zajavka.bankCalculator.creditCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Rate;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.MortgageTimePoint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class TimePointCalculationServiceImplTest {

    private TimePointCalculationService timePointCalculationService;

    public static Stream<Arguments> testTimePointData() {
        return Stream.of(
            arguments(
                LocalDate.of(2022, 6, 10),
                BigDecimal.valueOf(3),
                BigDecimal.valueOf(3),
                LocalDate.of(2022, 5, 10),
                BigDecimal.valueOf(27)),
            arguments(
                LocalDate.of(2052, 3, 23),
                BigDecimal.valueOf(8),
                BigDecimal.valueOf(5),
                LocalDate.of(2052, 2, 23),
                BigDecimal.valueOf(89)),
            arguments(
                LocalDate.of(2011, 12, 5),
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(12),
                LocalDate.of(2011, 11, 5),
                BigDecimal.valueOf(12)),
            arguments(
                LocalDate.of(2012, 1, 31),
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(2),
                LocalDate.of(2011, 12, 31),
                BigDecimal.valueOf(2))
        );
    }

    @BeforeEach
    public void setup() {
        this.timePointCalculationService = new TimePointCalculationServiceImpl();
    }

    @Test
    @DisplayName("Calculate first rate's time point")
    void shouldCalculateTimePointForFirstRateCorrectly() {
        //given
        InputData inputData1 = TestMortgageData.someInputData();

        MortgageTimePoint expected = TestMortgageData.someTimePoint();
        String expectedExceptionMessage = "This method only accepts rateNumber equal to ONE";

        //when

        MortgageTimePoint result = timePointCalculationService.calculate(BigDecimal.ONE, inputData1);
        Throwable exception = Assertions.assertThrows(RuntimeException.class,
            () -> timePointCalculationService.calculate(BigDecimal.TEN, inputData1));

        //then
        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());

    }

    @ParameterizedTest
    @MethodSource(value = "testTimePointData")
    @DisplayName("Calculate other rate's time point")
    void shouldCalculateTimePointForOtherRatesCorrectly(
        LocalDate expectedDate,
        BigDecimal year,
        BigDecimal month,
        LocalDate date,
        BigDecimal rateNumber
    ) {
        //given
        MortgageTimePoint mortgageTimePoint = TestMortgageData.someTimePoint()
            .withDate(date)
            .withYear(year)
            .withMonth(month);
        Rate rate = TestMortgageData.someRate().withMortgageTimePoint(mortgageTimePoint);
        MortgageTimePoint expected = mortgageTimePoint.withDate(expectedDate);

        //when
        MortgageTimePoint result = timePointCalculationService.calculate(rateNumber, rate);

        //then
        Assertions.assertEquals(expected, result);
    }
}