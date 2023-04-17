package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

import org.springframework.stereotype.Service;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.MortgageData;
import pl.zajavka.bankCalculator.calculators.creditCalculator.modelOfCredit.Overpayment;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
@Service
public class OverpaymentCalculationServiceImpl implements OverpaymentCalculationService {
    @Override
    public Overpayment calculate(BigDecimal rateNumber, MortgageData mortgageData) {
        BigDecimal overpaymentAmount = calculateAmount(rateNumber, mortgageData.overpaymentSchema())
            .orElse(BigDecimal.ZERO);
        BigDecimal overpaymentProvision = calculateProvision(rateNumber, overpaymentAmount, mortgageData);
        return new Overpayment(overpaymentAmount, overpaymentProvision);
    }

    private Optional<BigDecimal> calculateAmount(BigDecimal rateNumber, Map<Integer, BigDecimal> overPaymentSchema) {
        for (Map.Entry<Integer, BigDecimal> entry : overPaymentSchema.entrySet()) {
            if (rateNumber.equals(BigDecimal.valueOf(entry.getKey()))) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    private BigDecimal calculateProvision(BigDecimal rateNumber, BigDecimal overpaymentAmount, MortgageData mortgageData) {
        if (overpaymentAmount.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        if (rateNumber.compareTo(mortgageData.overpaymentProvisionMonths()) > 0) {
            return BigDecimal.ZERO;
        }
        return overpaymentAmount.multiply(mortgageData.overpaymentProvisionPercent());
    }


}
