package com.pei.pharmatest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "drugs")
public class Drug {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "manufacturer", nullable = false)
  private String manufacturer;

  @Column(name = "batch_number", nullable = false)
  private String batchNumber;

  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;

  @Column(name = "stock", nullable = false)
  private Integer stock;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

}