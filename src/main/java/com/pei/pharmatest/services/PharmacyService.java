package com.pei.pharmatest.services;

import java.util.List;
import com.pei.pharmatest.dto.PharmacyResponse;
import com.pei.pharmatest.dto.PrescriptionRequest;
import com.pei.pharmatest.dto.PrescriptionResponse;
import com.pei.pharmatest.exceptions.BusinessException;
import com.pei.pharmatest.exceptions.ResourceNotFoundException;
import com.pei.pharmatest.exceptions.ValidationException;

/**
 * Service interface for managing pharmacies and prescriptions in the system. Provides operations
 * for retrieving pharmacy information and managing prescriptions.
 */
public interface PharmacyService {

  /**
   * Retrieves all pharmacies in the system.
   *
   * @return A list of pharmacy responses
   */
  List<PharmacyResponse> getAllPharmacies();

  /**
   * Creates a prescription for a patient at a specific pharmacy. Validates that: 1. The pharmacy
   * exists 2. All requested drugs are contracted with the pharmacy 3. All drugs are within
   * allocation limits 4. All drugs are available in stock
   *
   * @param pharmacyId The ID of the pharmacy
   * @param request The prescription request containing drug details
   * @return The created prescription details
   * @throws ValidationException if input validation fails
   * @throws BusinessException if business rules are violated
   * @throws ResourceNotFoundException if pharmacy or patient not found
   */
  PrescriptionResponse createPrescription(Long pharmacyId, PrescriptionRequest request);

  /**
   * Fulfills a prescription by dispensing the prescribed drugs. Updates the prescription status and
   * reduces drug stock accordingly.
   *
   * @param prescriptionId The ID of the prescription to fulfill
   * @return The updated prescription details
   * @throws ResourceNotFoundException if prescription not found
   * @throws BusinessException if prescription cannot be fulfilled
   */
  PrescriptionResponse fulfillPrescription(Long prescriptionId);
}
