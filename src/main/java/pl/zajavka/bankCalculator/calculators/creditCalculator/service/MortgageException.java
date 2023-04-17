package pl.zajavka.bankCalculator.calculators.creditCalculator.service;

public class MortgageException extends RuntimeException {
    public MortgageException() {
        super("Case not handled");
    }
}
