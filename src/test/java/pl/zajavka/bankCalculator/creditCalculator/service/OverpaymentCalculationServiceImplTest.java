package pl.zajavka.bankCalculator.creditCalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;
import pl.zajavka.bankCalculator.calculators.creditCalculator.services.OverpaymentCalculationServiceImpl;
import pl.zajavka.bankCalculator.fixtures.TestMortgageData;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
@ExtendWith(MockitoExtension.class)
class OverpaymentCalculationServiceImplTest {

    @InjectMocks
    OverpaymentCalculationServiceImpl overpaymentCalculationService;

    public static Stream<Arguments> testData() {
        return Stream.of(
            arguments(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                Collections.emptyMap(),
                BigDecimal.ZERO),
            arguments(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                Collections.emptyMap(),
                BigDecimal.ONE),
            arguments(
                BigDecimal.valueOf(12012),
                BigDecimal.valueOf(480.48),
                Map.of(
                    12,BigDecimal.valueOf(12012)
                ),
                BigDecimal.valueOf(12)),
            arguments(
                BigDecimal.valueOf(2134),
                BigDecimal.ZERO,
                Map.of(
                    1,BigDecimal.valueOf(11401),
                    4,BigDecimal.valueOf(16586),
                    17,BigDecimal.valueOf(8971),
                    35,BigDecimal.valueOf(2134)
                ),
                BigDecimal.valueOf(35))
        );
    }


    @ParameterizedTest
    @MethodSource(value = "testData")
    void shouldCalculateOverpaymentCorrectly(
        BigDecimal expectedAmount,
        BigDecimal expectedProvision,
        Map<Integer, BigDecimal> overpaymentSchema,
        BigDecimal rateNumber
    ) {
        //given
        MortgageData mortgageData = TestMortgageData.someInputData()
            .withOverpaymentSchema(overpaymentSchema);
        Overpayment expected = TestMortgageData.someOverpayment()
            .withAmount(expectedAmount)
            .withProvisionAmount(expectedProvision);

        //when
        Overpayment result = overpaymentCalculationService.calculate(rateNumber, mortgageData);

        //then
        Assertions.assertEquals(expected, result);

    }
}