package com.pei.pharmatest.exceptions;

import com.pei.pharmatest.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the application. Provides centralized exception handling across all
 * controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handles ResourceNotFoundException by returning a 404 Not Found response.
   *
   * @param ex The ResourceNotFoundException that was thrown
   * @return ResponseEntity containing the error details
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse("Resource not found", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles IllegalArgumentException by returning a 400 Bad Request response.
   *
   * @param ex The IllegalArgumentException that was thrown
   * @return ResponseEntity containing the error details
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    ErrorResponse errorResponse = new ErrorResponse("Invalid input", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles IllegalStateException by returning a 400 Bad Request response.
   *
   * @param ex The IllegalStateException that was thrown
   * @return ResponseEntity containing the error details
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
    ErrorResponse errorResponse = new ErrorResponse("Invalid state", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles all other exceptions by returning a 500 Internal Server Error response.
   *
   * @param ex The Exception that was thrown
   * @return ResponseEntity containing the error details
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        "Internal server error",
        "An unexpected error occurred while processing your request"
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}