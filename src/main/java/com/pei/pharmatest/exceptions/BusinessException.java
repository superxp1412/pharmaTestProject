package com.pei.pharmatest.exceptions;

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

}
