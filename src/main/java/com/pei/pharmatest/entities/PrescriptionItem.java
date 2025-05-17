package com.pei.pharmatest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prescription_items")
public class PrescriptionItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "prescription_id", nullable = false)
  private Prescription prescription;

  @ManyToOne
  @JoinColumn(name = "drug_id", nullable = false)
  private Drug drug;

  @Column(name = "dosage", nullable = false)
  private String dosage;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;
}