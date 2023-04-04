package pl.zajavka.bankCalculator.creditCalculator.service;

public class MortgageException extends RuntimeException {
    public MortgageException() {
        super("Case not handled");
    }
}
