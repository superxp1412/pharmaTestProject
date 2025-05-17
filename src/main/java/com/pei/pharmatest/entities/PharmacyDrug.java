package com.pei.pharmatest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pharmacy_drugs")
public class PharmacyDrug {

  @EmbeddedId
  private PharmacyDrugId id;

  @ManyToOne
  @MapsId("pharmacyId")
  @JoinColumn(name = "pharmacy_id")
  private Pharmacy pharmacy;

  @ManyToOne
  @MapsId("drugId")
  @JoinColumn(name = "drug_id")
  private Drug drug;

  @Column(name = "allocated_amount", nullable = false)
  private Integer allocatedAmount;
}