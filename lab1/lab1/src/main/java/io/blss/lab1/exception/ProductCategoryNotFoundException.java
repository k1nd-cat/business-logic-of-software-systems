package io.blss.lab1.exception;

public class ProductCategoryNotFoundException extends RuntimeException {
    ProductCategoryNotFoundException(String message) { super(message); }
}
