package pl.zajavka.bankCalculator.creditCalculator.service;

import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.InputData;
import pl.zajavka.bankCalculator.creditCalculator.modelOfCredit.Overpayment;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class OverpaymentCalculationServiceImpl implements OverpaymentCalculationService {
    @Override
    public Overpayment calculate(BigDecimal rateNumber, InputData inputData) {
        BigDecimal overpaymentAmount = calculateAmount(rateNumber, inputData.overpaymentSchema())
            .orElse(BigDecimal.ZERO);
        BigDecimal overpaymentProvision = calculateProvision(rateNumber, overpaymentAmount, inputData);
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

    private BigDecimal calculateProvision(BigDecimal rateNumber, BigDecimal overpaymentAmount, InputData inputData) {
        if (overpaymentAmount.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        if (rateNumber.compareTo(inputData.overpaymentProvisionMonths()) > 0) {
            return BigDecimal.ZERO;
        }
        return overpaymentAmount.multiply(inputData.overpaymentProvisionPercent());
    }


}
