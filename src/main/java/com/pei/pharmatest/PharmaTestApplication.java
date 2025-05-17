package com.pei.pharmatest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the PharmaTest application. This is the entry point for the Spring
 * Boot application.
 */
@SpringBootApplication
public class PharmaTestApplication {

  /**
   * Main method that starts the Spring Boot application.
   *
   * @param args Command line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(PharmaTestApplication.class, args);
  }
}
