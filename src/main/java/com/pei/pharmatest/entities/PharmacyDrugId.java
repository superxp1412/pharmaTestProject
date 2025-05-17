package com.pei.pharmatest.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class PharmacyDrugId implements Serializable {

  private Long pharmacyId;
  private Long drugId;
}