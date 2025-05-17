package com.pei.pharmatest.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing a pharmacy response. Contains information about a pharmacy
 * including its identification, location details, and list of contracted drugs.
 */
@Getter
@Setter
public class PharmacyResponse {

  private Long id;
  private String name;
  private String address;
  private Set<ContractedDrugResponse> contractedDrugs;
}