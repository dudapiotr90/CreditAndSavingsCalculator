package pl.zajavka.bankCalculator.creditCalculator.modelOfCredit;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;

@With
@Builder
public record Overpayment(BigDecimal amount, BigDecimal provisionAmount) {
    public static final String REDUCE_RATE = "REDUCE_RATE";
    public static final String REDUCE_PERIOD = "REDUCE_PERIOD";

}
