package com.pei.pharmatest.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing a drug in a prescription response. Contains information about a
 * prescribed drug including its identification, name, manufacturer, batch number, and prescribed
 * quantity.
 */
@Getter
@Setter
public class PrescriptionDrugResponse {

  private Long drugId;
  private String name;
  private String manufacturer;
  private String batchNumber;
  private Integer quantity;
}