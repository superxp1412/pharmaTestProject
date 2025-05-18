package com.pei.pharmatest.exceptions;

import java.util.HashMap;
import java.util.Map;
import com.pei.pharmatest.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the application. Provides centralized exception handling across all
 * controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles ResourceNotFoundException by returning a 404 Not Found response.
   *
   * @param ex The ResourceNotFoundException that was thrown
   * @return ResponseEntity containing the error details
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    logger.warn("Resource not found: {}", ex.getMessage());
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
    logger.warn("Invalid input: {}", ex.getMessage());
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
    logger.warn("Invalid state: {}", ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse("Invalid state", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles validation exceptions by returning a 400 Bad Request response with detailed field
   * errors.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    Map<String, String> fieldErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      fieldErrors.put(fieldName, errorMessage);
    });

    logger.warn("Validation failed: {}", fieldErrors);
    ErrorResponse errorResponse = new ErrorResponse("Validation failed", fieldErrors.toString());
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
    logger.error("Unexpected error occurred", ex);
    ErrorResponse errorResponse = new ErrorResponse("Internal server error",
        "An unexpected error occurred while processing your request");
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
