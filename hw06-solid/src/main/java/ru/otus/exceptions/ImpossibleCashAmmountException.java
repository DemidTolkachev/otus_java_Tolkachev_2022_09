package ru.otus.exceptions;

public class ImpossibleCashAmmountException extends RuntimeException {

    private int amount;

    public int getAmount() {
        return amount;
    }

    public ImpossibleCashAmmountException(String message, int amount) {
        super(message);
        this.amount = amount;
    }
}
