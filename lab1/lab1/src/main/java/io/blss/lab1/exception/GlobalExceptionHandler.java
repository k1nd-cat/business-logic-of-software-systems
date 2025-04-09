package io.blss.lab1.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CartItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCartItemNotFoundException (CartItemNotFoundException e) {
        return new ErrorResponse(
                "Cart item not found",
                e.getMessage()
        );
    }


    @ExceptionHandler(CartItemQuantityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCartItemQuantityException (CartItemQuantityException e) {
        return new ErrorResponse(
                "Unavailable product quantity in cart",
                e.getMessage()
        );
    }

    @ExceptionHandler(InvalidPromoCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidPromoCodeException (InvalidPromoCodeException e) {
        return new ErrorResponse(
                "Invalid promo code",
                e.getMessage()
        );
    }

    @ExceptionHandler(OrderNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOrderNotAvailableException (OrderNotAvailableException e) {
        return new ErrorResponse(
                "Order not available",
                e.getMessage()
        );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOrderNotFoundException (OrderNotFoundException e) {
        return new ErrorResponse(
                "Order not found",
                e.getMessage()
        );
    }

    @ExceptionHandler(ProductCategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductCategoryNotFoundException (ProductCategoryNotFoundException e) {
        return new ErrorResponse(
                "Product category not found",
                e.getMessage()
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundException (ProductNotFoundException e) {
        return new ErrorResponse(
                "Product not found",
                e.getMessage()
        );
    }

    @ExceptionHandler(ProductQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductQuantityException (ProductQuantityException e) {
        return new ErrorResponse(
                "Unavailable product quantity",
                e.getMessage()
        );
    }

    @ExceptionHandler(PromoCodeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePromoCodeNotFoundException (PromoCodeNotFoundException e) {
        return new ErrorResponse(
                "Promo code not found",
                e.getMessage()
        );
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUsernameAlreadyExistsException (UsernameAlreadyExistsException e) {
        return new ErrorResponse(
                "Username already exists",
                e.getMessage()
        );
    }
}
