package com.pei.pharmatest.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing a prescription response. Contains information about a
 * prescription including its identification, associated pharmacy and patient details, creation
 * timestamp, status, and list of prescribed drugs.
 */
@Getter
@Setter
public class PrescriptionResponse {

  private Long id;
  private Long pharmacyId;
  private String pharmacyName;
  private Long patientId;
  private String patientName;
  private LocalDateTime createdAt;
  private String status;
  private List<PrescriptionDrugResponse> drugs;
}