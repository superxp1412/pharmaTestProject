package com.pei.pharmatest.dto;

import java.time.LocalDate;

public class DrugRequest {

  private String name;
  private String manufacturer;
  private String batchNumber;
  private LocalDate expiryDate;
  private Integer stock;

  // Getters
  public String getName() {
    return name;
  }

  // Setters
  public void setName(String name) {
    this.name = name;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public String getBatchNumber() {
    return batchNumber;
  }

  public void setBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
  }

  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }
}