package com.pei.pharmatest.exceptions;

/**
 * Exception thrown when a requested resource is not found in the system. This exception is
 * typically used when attempting to access or modify a resource that does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Constructs a new ResourceNotFoundException with the specified detail message.
   *
   * @param message The detail message explaining why the resource was not found
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}