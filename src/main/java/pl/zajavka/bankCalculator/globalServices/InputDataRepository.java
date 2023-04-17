package pl.zajavka.bankCalculator.globalServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.RateTypes;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.InterestCapitalization;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.savingsCalculator.modelOfSavings.SavingsType;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Repository
public class InputDataRepository {
    //    private final static Path MORTGAGE_DATA_CSV = Paths.get("src/main/resources/inputMortgageData.csv");
    private final static String MORTGAGE_DATA_CSV = "classpath:inputMortgageData.csv";

    //    private final static Path SAVINGS_DATA_CSV = Paths.get("src/main/resources/inputSavingsData.csv");
    private final static String SAVINGS_DATA_CSV = "classpath:inputSavingsData.csv";

//    private static Map<String, List<String>> groupFileToMap(Stream<String> inputDataLines) {
//        return inputDataLines
//            .collect(Collectors.groupingBy(line -> line.split(";")[0]));
//    }

    public Optional<InputData> readMortgageDataFile() {

        Map<String, List<String>> mortgageFileContent = readFile(MORTGAGE_DATA_CSV);
//        try (
//            Stream<String> inputDataLines = Files.lines(MORTGAGE_DATA_CSV)
//        ) {
//            mortgageFileContent = groupFileToMap(inputDataLines);
//        }

        firstValidateInputDataFile(mortgageFileContent);
        secondValidateInputDataFile(mortgageFileContent);

        Map<String, String> inputData = extractProperMapValues(mortgageFileContent);

        return Optional.ofNullable(InputData.builder()
            .repaymentStartDate(Optional.ofNullable(
                inputData.get("repaymentStartDate")).map(LocalDate::parse).orElseThrow())
            .monthsDuration(Optional.ofNullable(
                inputData.get("monthsDuration")).map(BigDecimal::new).orElseThrow())
            .amount(Optional.ofNullable(
                inputData.get("amount")).map(BigDecimal::new).orElseThrow())
            .rateType(Optional.ofNullable(
                inputData.get("rateType")).map(RateTypes::valueOf).orElseThrow())
            .wiborPercentage(Optional.ofNullable(
                inputData.get("wiborPercentage")).map(BigDecimal::new).orElseThrow())
            .bankMarginPercent(Optional.ofNullable(
                inputData.get("bankMarginPercent")).map(BigDecimal::new).orElseThrow())
            .overpaymentReduceWay(Optional.ofNullable(
                inputData.get("overpaymentReduceWay")).orElseThrow())
            .overpaymentProvisionPercent(Optional.ofNullable(
                inputData.get("overpaymentProvisionPercent")).map(BigDecimal::new).orElseThrow())
            .overpaymentProvisionMonths(Optional.ofNullable(
                inputData.get("overpaymentProvisionMonths")).map(BigDecimal::new).orElseThrow())
            .overpaymentSchema(Optional.ofNullable(
                inputData.get("overpaymentSchema")).map(this::createSchema).orElse(Collections.emptyMap()))
            .mortgagePrintPayoffsSchedule(Optional.ofNullable(
                inputData.get("mortgagePrintPayoffsSchedule")).map(Boolean::parseBoolean).orElseThrow())
            .mortgageRateNumberToPrint(Optional.ofNullable(
                inputData.get("mortgageRateNumberToPrint")).map(Integer::parseInt).orElseThrow())
            .compareToSavings(Optional.ofNullable(
                inputData.get("compareToSavings")).map(Boolean::parseBoolean).orElseThrow())
            .build());

    }

    public Optional<SavingsData> readSavingsDataFile() {
        Map<String, List<String>> savingsFileContent = readFile(SAVINGS_DATA_CSV);
//        Map<String, List<String>> savingsFileContent = groupFileToMap(Files.readString(SAVINGS_DATA_CSV).lines());

        firstValidateInputDataFile(savingsFileContent);
        secondValidateInputDataFile(savingsFileContent);

        Map<String, String> savingsData = extractProperMapValues(savingsFileContent);

        return Optional.ofNullable(SavingsData.builder()
            .depositDuration(Optional.ofNullable(
                savingsData.get("depositDuration")).map(BigDecimal::new).orElseThrow())
            .depositSchema(Optional.ofNullable(
                savingsData.get("depositSchema")).map(this::createSchema).orElse(Collections.emptyMap()))
            .savingType(Optional.ofNullable(
                savingsData.get("savingType")).map(SavingsType::valueOf).orElseThrow())
            .depositInterest(Optional.ofNullable(
                savingsData.get("depositInterest")).map(BigDecimal::new).orElseThrow())
            .interestCapitalization(Optional.ofNullable(
                savingsData.get("interestCapitalization")).map(InterestCapitalization::valueOf).orElseThrow())
            .tax(Optional.ofNullable(
                savingsData.get("tax")).map(BigDecimal::new).orElseThrow())
            .build());
    }

    private Map<String, List<String>> readFile(String dataFile) {
        try {
            return Files.readString(ResourceUtils.getFile(dataFile).toPath())
                .lines()
                .collect(Collectors.groupingBy(line -> line.split(";")[0]));
        } catch (Exception e) {
            log.error("Error loading inputData, stopping program! Error: [{}]", e.getMessage());
            return Map.of();
        }
    }

    private Map<String, String> extractProperMapValues(Map<String, List<String>> fileContent) {
        return fileContent.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().get(0).split(";")[1]
            ));
    }

    private void firstValidateInputDataFile(Map<String, List<String>> fileContent) {
        if (fileContent.values().stream()
            .anyMatch(value -> value.size() != 1)) {
            throw new IllegalArgumentException("Data: has wrong configuration%n" + System.lineSeparator());
        }
    }

    private void secondValidateInputDataFile(Map<String, List<String>> content) {
        if (content.values().stream()
            .filter(value -> value.get(0).split(";").length > 2)
            .map(value -> System.err.printf("Data: has wrong configuration: [%s]%n", value))
            .findAny()
            .isPresent()) {
            throw new IllegalArgumentException("Configuration mismatch");
        }
    }

    private Map<Integer, BigDecimal> createSchema(String schema) {
        return Stream.of(schema.split(","))
            .map(entry -> Map.entry(entry.split(":")[0], entry.split(":")[1]))
            .collect(Collectors.toMap(
                mapEntry -> Integer.parseInt(mapEntry.getKey()),
                mapEntry -> new BigDecimal(mapEntry.getValue()),
                (v1, v2) -> v1,
                TreeMap::new
            ));
    }
}
