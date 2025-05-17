package com.pei.pharmatest.controllers;

import com.pei.pharmatest.dto.DrugRequest;
import com.pei.pharmatest.dto.DrugResponse;
import com.pei.pharmatest.dto.ErrorResponse;
import com.pei.pharmatest.services.DrugService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing drug-related operations. Provides endpoints for retrieving and
 * adding drugs to the system.
 */
@RestController
@RequestMapping("/api/v1/drugs")
public class DrugController {

  private final DrugService drugService;

  public DrugController(DrugService drugService) {
    this.drugService = drugService;
  }

  /**
   * Retrieves a drug by its ID.
   *
   * @param id The ID of the drug to retrieve
   * @return ResponseEntity containing the drug details if found, or an error response
   */
  @GetMapping("/{id}")
  public ResponseEntity<Object> getDrug(@PathVariable Long id) {
    try {
      if (id == null || id <= 0) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("Invalid input", "Drug ID must be a positive number"));
      }

      Optional<DrugResponse> drug = drugService.getDrug(id);
      if (drug.isPresent()) {
        return ResponseEntity.ok().body(drug.get());
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ErrorResponse(
              "Drug not found",
              String.format("Drug with ID %d does not exist in the inventory", id)
          ));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse("Invalid input", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse(
              "Internal server error",
              "An unexpected error occurred while processing your request"
          ));
    }
  }

  /**
   * Adds a new drug to the system.
   *
   * @param request The drug request containing the drug details
   * @return ResponseEntity containing the created drug details or an error response
   */
  @PostMapping
  public ResponseEntity<?> addDrug(@RequestBody DrugRequest request) {
    try {
      DrugResponse response = drugService.addDrug(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse("Invalid input", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Internal server error", e.getMessage()));
    }
  }
}