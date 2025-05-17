package com.pei.pharmatest.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prescriptions")
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "pharmacy_id", nullable = false)
  private Pharmacy pharmacy;

  @ManyToOne
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private PrescriptionStatus status;

  @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PrescriptionItem> items = new HashSet<>();

  public enum PrescriptionStatus {
    CREATED, FULFILLED, CANCELLED
  }
}