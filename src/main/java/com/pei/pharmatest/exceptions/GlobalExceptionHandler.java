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
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    logger.warn("Resource not found: {}", ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse("Resource not found", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles ValidationException by returning a 400 Bad Request response.
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
    logger.warn("Validation error: {}", ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse("Validation error", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles BusinessException by returning a 400 Bad Request response.
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    logger.warn("Business rule violation: {}", ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse("Business rule violation", ex.getMessage());
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
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    logger.error("Unexpected error occurred", ex);
    ErrorResponse errorResponse = new ErrorResponse("Internal server error",
        "An unexpected error occurred while processing your request");
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
