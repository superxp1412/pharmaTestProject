package com.pei.pharmatest.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest {

  private String name;
  private LocalDate dateOfBirth;
  private String address;
  private String phoneNumber;
  private String email;
}