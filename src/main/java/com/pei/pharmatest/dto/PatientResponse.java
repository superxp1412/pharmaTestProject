package com.pei.pharmatest.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing a patient response. Contains information about a patient
 * including their personal details, contact information, and identification.
 */
@Getter
@Setter
public class PatientResponse {

  private Long id;
  private String name;
  private LocalDate dateOfBirth;
  private String address;
  private String phoneNumber;
  private String email;
}