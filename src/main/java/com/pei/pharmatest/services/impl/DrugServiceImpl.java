package com.pei.pharmatest.services.impl;

import com.pei.pharmatest.dto.DrugRequest;
import com.pei.pharmatest.dto.DrugResponse;
import com.pei.pharmatest.entities.Drug;
import com.pei.pharmatest.repositories.DrugRepository;
import com.pei.pharmatest.services.DrugService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implementation of the DrugService interface. Handles drug-related operations including retrieval
 * and addition of drugs to the system.
 */
@Service
public class DrugServiceImpl implements DrugService {

  private final DrugRepository drugRepository;

  public DrugServiceImpl(DrugRepository drugRepository) {
    this.drugRepository = drugRepository;
  }

  /**
   * Retrieves a drug by its ID.
   *
   * @param id The ID of the drug to retrieve
   * @return An Optional containing the drug response if found, empty otherwise
   */
  @Override
  public Optional<DrugResponse> getDrug(Long id) {
    return drugRepository.findById(id)
        .map(this::convertToResponse);
  }

  /**
   * Adds a new drug to the system.
   *
   * @param request The drug request containing the drug details
   * @return The created drug response
   * @throws IllegalArgumentException if the request validation fails
   */
  @Override
  public DrugResponse addDrug(DrugRequest request) {
    validateRequest(request);
    Drug drug = convertToEntity(request);
    Drug savedDrug = drugRepository.save(drug);
    return convertToResponse(savedDrug);
  }

  /**
   * Validates the drug request.
   *
   * @param request The drug request to validate
   * @throws IllegalArgumentException if any validation fails
   */
  private void validateRequest(DrugRequest request) {
    if (request.getName() == null || request.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Drug name is required");
    }
    if (request.getManufacturer() == null || request.getManufacturer().trim().isEmpty()) {
      throw new IllegalArgumentException("Manufacturer name is required");
    }
    if (request.getBatchNumber() == null || request.getBatchNumber().trim().isEmpty()) {
      throw new IllegalArgumentException("Batch number is required");
    }
    if (request.getExpiryDate() == null) {
      throw new IllegalArgumentException("Expiry date is required");
    }
    if (request.getStock() == null) {
      throw new IllegalArgumentException("Stock quantity is required");
    }
    if (request.getStock() < 0) {
      throw new IllegalArgumentException("Stock quantity cannot be negative");
    }
    if (request.getExpiryDate().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException(
          "Cannot add expired drugs to inventory. The drug has already expired on "
              + request.getExpiryDate().toString());
    }
  }

  /**
   * Converts a drug request to a drug entity.
   *
   * @param request The drug request to convert
   * @return The created drug entity
   */
  private Drug convertToEntity(DrugRequest request) {
    Drug drug = new Drug();
    drug.setName(request.getName());
    drug.setManufacturer(request.getManufacturer());
    drug.setBatchNumber(request.getBatchNumber());
    drug.setExpiryDate(request.getExpiryDate());
    drug.setStock(request.getStock());
    drug.setCreatedAt(LocalDateTime.now());
    return drug;
  }

  /**
   * Converts a drug entity to a drug response.
   *
   * @param drug The drug entity to convert
   * @return The created drug response
   */
  private DrugResponse convertToResponse(Drug drug) {
    DrugResponse response = new DrugResponse();
    response.setId(drug.getId());
    response.setName(drug.getName());
    response.setManufacturer(drug.getManufacturer());
    response.setBatchNumber(drug.getBatchNumber());
    response.setExpiryDate(drug.getExpiryDate());
    response.setStock(drug.getStock());
    response.setCreatedAt(drug.getCreatedAt());
    return response;
  }
}