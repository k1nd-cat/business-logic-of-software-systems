package io.blss.labs.exception;

public class OrderNotAvailableException extends IllegalArgumentException {
    public OrderNotAvailableException(String message) { super(message); }
}
