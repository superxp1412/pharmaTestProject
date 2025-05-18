package com.pei.pharmatest.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pei.pharmatest.dto.PrescriptionDrugRequest;
import com.pei.pharmatest.dto.PrescriptionRequest;
import com.pei.pharmatest.dto.PrescriptionResponse;
import com.pei.pharmatest.entities.Drug;
import com.pei.pharmatest.entities.Patient;
import com.pei.pharmatest.entities.Pharmacy;
import com.pei.pharmatest.entities.PharmacyDrug;
import com.pei.pharmatest.exceptions.BusinessException;
import com.pei.pharmatest.exceptions.ResourceNotFoundException;
import com.pei.pharmatest.repositories.PatientRepository;
import com.pei.pharmatest.repositories.PharmacyRepository;
import com.pei.pharmatest.repositories.PrescriptionRepository;
import com.pei.pharmatest.services.impl.PharmacyServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

  @Mock
  private PharmacyRepository pharmacyRepository;

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private PrescriptionRepository prescriptionRepository;

  @InjectMocks
  private PharmacyServiceImpl pharmacyService;

  private Pharmacy pharmacy;
  private Patient patient;

  @BeforeEach
  void setUp() {
    // Setup test data
    pharmacy = new Pharmacy();
    pharmacy.setId(1L);
    pharmacy.setName("Test Pharmacy");

    Drug drug1 = new Drug();
    drug1.setId(1L);
    drug1.setName("Drug A");
    drug1.setStock(100);

    Drug drug2 = new Drug();
    drug2.setId(2L);
    drug2.setName("Drug B");
    drug2.setStock(50);

    Drug drug3 = new Drug();
    drug3.setId(3L);
    drug3.setName("Drug C");
    drug3.setStock(50);

    PharmacyDrug pharmacyDrug1 = new PharmacyDrug();
    pharmacyDrug1.setPharmacy(pharmacy);
    pharmacyDrug1.setDrug(drug1);
    pharmacyDrug1.setAllocatedAmount(80);

    PharmacyDrug pharmacyDrug2 = new PharmacyDrug();
    pharmacyDrug2.setPharmacy(pharmacy);
    pharmacyDrug2.setDrug(drug2);
    pharmacyDrug2.setAllocatedAmount(40);

    PharmacyDrug pharmacyDrug3 = new PharmacyDrug();
    pharmacyDrug3.setPharmacy(pharmacy);
    pharmacyDrug3.setDrug(drug3);
    pharmacyDrug3.setAllocatedAmount(60);

    pharmacy.setPharmacyDrugs(
        new HashSet<>(Arrays.asList(pharmacyDrug1, pharmacyDrug2, pharmacyDrug3)));

    // Setup patient data
    patient = new Patient();
    patient.setId(1L);
    patient.setName("John Doe");
    patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
  }

  @Test
  void createPrescription_WhenValidRequest_ShouldCreatePrescription() {
    // Given
    PrescriptionRequest request = new PrescriptionRequest();
    request.setPatientId(1L);

    PrescriptionDrugRequest drugRequest1 = new PrescriptionDrugRequest();
    drugRequest1.setDrugId(1L);
    drugRequest1.setQuantity(50);
    drugRequest1.setDosage("500ml");

    request.setDrugs(List.of(drugRequest1));

    when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
    when(prescriptionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    // When
    PrescriptionResponse response = pharmacyService.createPrescription(1L, request);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getPharmacyId()).isEqualTo(1L);
    assertThat(response.getPatientId()).isEqualTo(1L);
    assertThat(response.getDrugs()).hasSize(1);
    assertThat(response.getStatus()).isEqualTo("CREATED");
  }

  @Test
  void createPrescription_WhenPharmacyNotFound_ShouldThrowException() {
    // Given
    PrescriptionRequest request = new PrescriptionRequest();
    PrescriptionDrugRequest drugRequest1 = new PrescriptionDrugRequest();
    drugRequest1.setDrugId(1L);
    drugRequest1.setQuantity(50);
    drugRequest1.setDosage("500ml");
    request.setPatientId(1L);
    request.setDrugs(List.of(drugRequest1));
    when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> pharmacyService.createPrescription(1L, request))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Pharmacy not found with id: 1");
  }

  @Test
  void createPrescription_WhenPatientNotFound_ShouldThrowException() {
    // Given
    PrescriptionRequest request = new PrescriptionRequest();
    PrescriptionDrugRequest drugRequest1 = new PrescriptionDrugRequest();
    drugRequest1.setDrugId(1L);
    drugRequest1.setQuantity(50);
    drugRequest1.setDosage("500ml");
    request.setPatientId(999L);
    request.setDrugs(List.of(drugRequest1));

    when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
    when(patientRepository.findById(999L)).thenReturn(Optional.empty());

    // When/Then
    assertThatThrownBy(() -> pharmacyService.createPrescription(1L, request))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Patient not found with id: 999");
  }

  @Test
  void createPrescription_WhenDrugNotContracted_ShouldThrowException() {
    // Given
    PrescriptionRequest request = new PrescriptionRequest();
    request.setPatientId(1L);

    PrescriptionDrugRequest drugRequest = new PrescriptionDrugRequest();
    drugRequest.setDrugId(4L); // Non-contracted drug
    drugRequest.setQuantity(10);
    drugRequest.setDosage("500ml");

    request.setDrugs(List.of(drugRequest));

    when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

    // When/Then
    assertThatThrownBy(() -> pharmacyService.createPrescription(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessage("Drug with ID 4 is not contracted with this pharmacy");
  }

  @Test
  void createPrescription_WhenExceedingAllocation_ShouldThrowException() {
    // Given
    PrescriptionRequest request = new PrescriptionRequest();
    request.setPatientId(1L);

    PrescriptionDrugRequest drugRequest = new PrescriptionDrugRequest();
    drugRequest.setDrugId(1L);
    drugRequest.setQuantity(90); // Exceeds allocation of 80
    drugRequest.setDosage("500ml");

    request.setDrugs(List.of(drugRequest));

    when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

    // When/Then
    assertThatThrownBy(() -> pharmacyService.createPrescription(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessage("Requested quantity exceeds pharmacy's allocation for drug: Drug A");
  }

  @Test
  void createPrescription_WhenExceedingStock_ShouldThrowException() {
    // Given
    PrescriptionRequest request = new PrescriptionRequest();
    request.setPatientId(1L);

    PrescriptionDrugRequest drugRequest = new PrescriptionDrugRequest();
    drugRequest.setDrugId(3L);
    drugRequest.setQuantity(55); // Exceeds stock of 50 for drug3, which not exceeds allocation
    drugRequest.setDosage("500ml");

    request.setDrugs(List.of(drugRequest));

    when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

    // When/Then
    assertThatThrownBy(() -> pharmacyService.createPrescription(1L, request))
        .isInstanceOf(BusinessException.class)
        .hasMessage("Requested quantity exceeds available stock for drug: Drug C");
  }
}