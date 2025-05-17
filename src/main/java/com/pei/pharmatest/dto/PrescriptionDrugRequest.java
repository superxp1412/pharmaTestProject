package com.pei.pharmatest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDrugRequest {

  private Long drugId;
  private Integer quantity;
  private String dosage;
}