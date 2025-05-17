package com.pei.pharmatest.controllers;

import com.pei.pharmatest.dto.PharmacyResponse;
import com.pei.pharmatest.dto.PrescriptionRequest;
import com.pei.pharmatest.dto.PrescriptionResponse;
import com.pei.pharmatest.services.PharmacyService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing pharmacy-related operations. Provides endpoints for retrieving
 * pharmacy information and managing prescriptions.
 */
@RestController
@RequestMapping("/api/v1/pharmacies")
public class PharmacyController {

  private final PharmacyService pharmacyService;

  public PharmacyController(PharmacyService pharmacyService) {
    this.pharmacyService = pharmacyService;
  }

  /**
   * Retrieves all pharmacies in the system.
   *
   * @return ResponseEntity containing a list of pharmacy responses
   */
  @GetMapping
  public ResponseEntity<List<PharmacyResponse>> getAllPharmacies() {
    List<PharmacyResponse> pharmacies = pharmacyService.getAllPharmacies();
    return ResponseEntity.ok(pharmacies);
  }

  /**
   * Creates a new prescription for a patient at a specific pharmacy.
   *
   * @param pharmacyId The ID of the pharmacy
   * @param request    The prescription request containing drug details
   * @return ResponseEntity containing the created prescription details
   */
  @PostMapping("/{pharmacyId}/prescriptions")
  public ResponseEntity<PrescriptionResponse> createPrescription(
      @PathVariable Long pharmacyId,
      @RequestBody PrescriptionRequest request) {
    PrescriptionResponse prescription = pharmacyService.createPrescription(pharmacyId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(prescription);
  }

  /**
   * Fulfills a prescription by dispensing the prescribed drugs.
   *
   * @param prescriptionId The ID of the prescription to fulfill
   * @return ResponseEntity containing the updated prescription details
   */
  @PostMapping("/prescriptions/{prescriptionId}/fulfill")
  public ResponseEntity<PrescriptionResponse> fulfillPrescription(
      @PathVariable Long prescriptionId) {
    PrescriptionResponse prescription = pharmacyService.fulfillPrescription(prescriptionId);
    return ResponseEntity.ok(prescription);
  }
}