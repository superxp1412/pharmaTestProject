package com.pei.pharmatest.services;

import com.pei.pharmatest.dto.DrugRequest;
import com.pei.pharmatest.dto.DrugResponse;
import java.util.Optional;

/**
 * Service interface for managing drugs in the system. Provides operations for retrieving and adding
 * drugs.
 */
public interface DrugService {

  /**
   * Retrieves a drug by its ID.
   *
   * @param id The ID of the drug to retrieve
   * @return An Optional containing the drug response if found, empty otherwise
   */
  Optional<DrugResponse> getDrug(Long id);

  /**
   * Adds a new drug to the system.
   *
   * @param request The drug request containing the drug details
   * @return The created drug response
   * @throws IllegalArgumentException if the request validation fails
   */
  DrugResponse addDrug(DrugRequest request);
}