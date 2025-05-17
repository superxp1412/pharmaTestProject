package com.pei.pharmatest.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionRequest {

  private Long patientId;
  private List<PrescriptionDrugRequest> drugs;
}