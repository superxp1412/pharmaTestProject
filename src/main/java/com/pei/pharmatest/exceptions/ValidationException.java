package com.pei.pharmatest.exceptions;

/**
 * Exception thrown when validation of input data fails.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

}
