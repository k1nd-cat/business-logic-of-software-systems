package io.blss.lab1.exception;

public class OrderNotAvailableException extends IllegalArgumentException {
    public OrderNotAvailableException(String message) { super(message); }
}
