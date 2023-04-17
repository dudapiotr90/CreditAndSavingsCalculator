package pl.zajavka.bankCalculator.calculators.creditCalculator.services;

public class MortgageException extends RuntimeException {
    public MortgageException() {
        super("Case not handled");
    }
}
