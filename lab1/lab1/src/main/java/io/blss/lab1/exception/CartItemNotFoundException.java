package io.blss.lab1.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String message) { super(message); }
}
