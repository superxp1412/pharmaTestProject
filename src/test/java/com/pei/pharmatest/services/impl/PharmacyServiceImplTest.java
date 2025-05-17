package com.pei.pharmatest.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pei.pharmatest.dto.PrescriptionResponse;
import com.pei.pharmatest.entities.Drug;
import com.pei.pharmatest.entities.Patient;
import com.pei.pharmatest.entities.Pharmacy;
import com.pei.pharmatest.entities.Prescription;
import com.pei.pharmatest.entities.PrescriptionItem;
import com.pei.pharmatest.repositories.DrugRepository;
import com.pei.pharmatest.repositories.PatientRepository;
import com.pei.pharmatest.repositories.PharmacyRepository;
import com.pei.pharmatest.repositories.PrescriptionRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PharmacyServiceImplTest {

  @Mock
  PatientRepository patientRepository;
  @Mock
  private PharmacyRepository pharmacyRepository;
  @Mock
  private PrescriptionRepository prescriptionRepository;
  @Mock
  private DrugRepository drugRepository;
  private PharmacyServiceImpl pharmacyService;

  @BeforeEach
  void setUp() {
    pharmacyService = new PharmacyServiceImpl(pharmacyRepository, patientRepository,
        prescriptionRepository, drugRepository);
  }

  @Test
  void fulfillPrescription_Success() {
    // Given
    Long prescriptionId = 1L;

    Prescription prescription = new Prescription();
    prescription.setId(prescriptionId);
    Pharmacy pharmacy = new Pharmacy();
    pharmacy.setId(1L);
    prescription.setPharmacy(pharmacy);
    Patient patient = new Patient();
    patient.setId(1L);
    prescription.setPatient(patient);
    prescription.setStatus(Prescription.PrescriptionStatus.CREATED);

    Drug drug1 = new Drug();
    drug1.setId(1L);
    drug1.setStock(100);
    drug1.setExpiryDate(LocalDate.now().plusDays(30));

    PrescriptionItem item1 = new PrescriptionItem();
    item1.setPrescription(prescription);
    item1.setDrug(drug1);
    item1.setQuantity(10);

    prescription.setItems(Set.of(item1));

    when(prescriptionRepository.findById(prescriptionId))
        .thenReturn(Optional.of(prescription));
    when(prescriptionRepository.save(any(Prescription.class)))
        .thenReturn(prescription);
    when(drugRepository.save(any(Drug.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When
    PrescriptionResponse response = pharmacyService.fulfillPrescription(prescriptionId);

    // Then
    assertEquals(Prescription.PrescriptionStatus.FULFILLED.name(), response.getStatus());
    assertEquals(90, drug1.getStock()); // Stock should be reduced
    verify(prescriptionRepository).save(prescription);
  }

  @Test
  void fulfillPrescription_ExpiredDrug() {
    // Given
    Long prescriptionId = 1L;

    Prescription prescription = new Prescription();
    prescription.setId(prescriptionId);
    prescription.setStatus(Prescription.PrescriptionStatus.CREATED);

    Drug drug1 = new Drug();
    drug1.setId(1L);
    drug1.setStock(100);
    drug1.setExpiryDate(LocalDate.now().minusDays(1));

    PrescriptionItem item1 = new PrescriptionItem();
    item1.setPrescription(prescription);
    item1.setDrug(drug1);
    item1.setQuantity(10);

    prescription.setItems(Set.of(item1));

    when(prescriptionRepository.findById(prescriptionId))
        .thenReturn(Optional.of(prescription));

    // When & Then
    assertThrows(IllegalStateException.class,
        () -> pharmacyService.fulfillPrescription(prescriptionId));
  }

  @Test
  void fulfillPrescription_InsufficientStock() {
    // Given
    Long prescriptionId = 1L;

    Prescription prescription = new Prescription();
    prescription.setId(prescriptionId);
    prescription.setStatus(Prescription.PrescriptionStatus.CREATED);

    Drug drug1 = new Drug();
    drug1.setId(1L);
    drug1.setStock(5);
    drug1.setExpiryDate(LocalDate.now().plusDays(30));

    PrescriptionItem item1 = new PrescriptionItem();
    item1.setPrescription(prescription);
    item1.setDrug(drug1);
    item1.setQuantity(10);

    prescription.setItems(Set.of(item1));

    when(prescriptionRepository.findById(prescriptionId))
        .thenReturn(Optional.of(prescription));

    // When & Then
    assertThrows(IllegalStateException.class,
        () -> pharmacyService.fulfillPrescription(prescriptionId));
  }
}