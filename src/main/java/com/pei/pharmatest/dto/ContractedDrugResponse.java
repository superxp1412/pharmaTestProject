package com.pei.pharmatest.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractedDrugResponse {

  private Long id;
  private String name;
  private String manufacturer;
  private String batchNumber;
  private LocalDate expiryDate;
  private Integer stock;
  private Integer allocatedAmount;
}