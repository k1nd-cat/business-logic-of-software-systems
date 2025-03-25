package io.blss.lab1.exception;

public class ProductNotFoundException extends RuntimeException {
    ProductNotFoundException(String message) { super(message); }
}
