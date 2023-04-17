package pl.zajavka.bankCalculator.calculators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.commonServices.InputDataRepository;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.service.MortgageCalculationService;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.modelOfSavings.SavingsData;
import pl.zajavka.bankCalculator.calculators.savingsCalculator.service.SavingsCalculationService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankCalculatorServiceImpl implements BankCalculatorService {
    private final InputDataRepository inputDataRepository;
    private final MortgageCalculationService mortgageCalculationService;
    private final SavingsCalculationService savingsCalculationService;

    @Override
    public void calculate() {
        Optional<MortgageData> mortgageData = inputDataRepository.readMortgageDataFile();
        if (mortgageData.isEmpty()) {
            return;
        }
        mortgageCalculationService.calculate(mortgageData.get());

        Optional<SavingsData> savingsData = inputDataRepository.readSavingsDataFile();
        if (savingsData.isEmpty()) {
            return;
        }
        savingsCalculationService.calculate(savingsData.get(),mortgageData.get());
    }
}
