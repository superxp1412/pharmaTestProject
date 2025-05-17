package com.pei.pharmatest.services;

import com.pei.pharmatest.dto.PrescriptionRequest;
import com.pei.pharmatest.dto.PrescriptionResponse;

/**
 * Service interface for managing prescriptions in the system. Provides operations for creating and
 * retrieving prescriptions.
 */
public interface PrescriptionService {

  /**
   * Creates a new prescription.
   *
   * @param pharmacyId The ID of the pharmacy
   * @param request    The prescription request
   * @return The created prescription
   */
  PrescriptionResponse createPrescription(Long pharmacyId, PrescriptionRequest request);

  /**
   * Retrieves a prescription by its ID.
   *
   * @param id The prescription ID
   * @return The prescription details
   */
  PrescriptionResponse getPrescription(Long id);
}