package com.pei.pharmatest.services.impl;

import com.pei.pharmatest.dto.ContractedDrugResponse;
import com.pei.pharmatest.dto.PharmacyResponse;
import com.pei.pharmatest.dto.PrescriptionDrugRequest;
import com.pei.pharmatest.dto.PrescriptionDrugResponse;
import com.pei.pharmatest.dto.PrescriptionRequest;
import com.pei.pharmatest.dto.PrescriptionResponse;
import com.pei.pharmatest.entities.Drug;
import com.pei.pharmatest.entities.Patient;
import com.pei.pharmatest.entities.Pharmacy;
import com.pei.pharmatest.entities.PharmacyDrug;
import com.pei.pharmatest.entities.Prescription;
import com.pei.pharmatest.entities.PrescriptionItem;
import com.pei.pharmatest.exceptions.ResourceNotFoundException;
import com.pei.pharmatest.repositories.DrugRepository;
import com.pei.pharmatest.repositories.PatientRepository;
import com.pei.pharmatest.repositories.PharmacyRepository;
import com.pei.pharmatest.repositories.PrescriptionRepository;
import com.pei.pharmatest.services.PharmacyService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the PharmacyService interface. Handles pharmacy-related operations including
 * prescription management and pharmacy information retrieval.
 */
@Service
public class PharmacyServiceImpl implements PharmacyService {

  private final PharmacyRepository pharmacyRepository;
  private final PatientRepository patientRepository;
  private final PrescriptionRepository prescriptionRepository;
  private final DrugRepository drugRepository;

  /**
   * Constructs a new PharmacyServiceImpl with the required dependencies.
   *
   * @param pharmacyRepository     The repository for pharmacy operations
   * @param patientRepository      The repository for patient operations
   * @param prescriptionRepository The repository for prescription operations
   * @param drugRepository         The repository for drug operations
   */
  public PharmacyServiceImpl(PharmacyRepository pharmacyRepository,
      PatientRepository patientRepository,
      PrescriptionRepository prescriptionRepository,
      DrugRepository drugRepository) {
    this.pharmacyRepository = pharmacyRepository;
    this.patientRepository = patientRepository;
    this.prescriptionRepository = prescriptionRepository;
    this.drugRepository = drugRepository;
  }

  /**
   * Retrieves all pharmacies in the system.
   *
   * @return A list of pharmacy responses containing pharmacy information
   */
  @Override
  public List<PharmacyResponse> getAllPharmacies() {
    return pharmacyRepository.findAll().stream()
        .map(this::mapToPharmacyResponse)
        .collect(Collectors.toList());
  }

  /**
   * Maps a Pharmacy entity to a PharmacyResponse DTO.
   *
   * @param pharmacy The pharmacy entity to map
   * @return A PharmacyResponse containing the pharmacy information
   */
  private PharmacyResponse mapToPharmacyResponse(Pharmacy pharmacy) {
    PharmacyResponse response = new PharmacyResponse();
    response.setId(pharmacy.getId());
    response.setName(pharmacy.getName());
    response.setAddress(pharmacy.getAddress());
    response.setContractedDrugs(mapToContractedDrugs(pharmacy.getPharmacyDrugs()));
    return response;
  }

  /**
   * Maps a set of PharmacyDrug entities to a set of ContractedDrugResponse DTOs.
   *
   * @param pharmacyDrugs The set of pharmacy drugs to map
   * @return A set of ContractedDrugResponse containing the contracted drug information
   */
  private java.util.Set<ContractedDrugResponse> mapToContractedDrugs(
      java.util.Set<PharmacyDrug> pharmacyDrugs) {
    return pharmacyDrugs.stream()
        .map(this::mapToContractedDrugResponse)
        .collect(Collectors.toSet());
  }

  /**
   * Maps a PharmacyDrug entity to a ContractedDrugResponse DTO.
   *
   * @param pharmacyDrug The pharmacy drug entity to map
   * @return A ContractedDrugResponse containing the contracted drug information
   */
  private ContractedDrugResponse mapToContractedDrugResponse(PharmacyDrug pharmacyDrug) {
    Drug drug = pharmacyDrug.getDrug();
    ContractedDrugResponse response = new ContractedDrugResponse();
    response.setId(drug.getId());
    response.setName(drug.getName());
    response.setManufacturer(drug.getManufacturer());
    response.setBatchNumber(drug.getBatchNumber());
    response.setExpiryDate(drug.getExpiryDate());
    response.setStock(drug.getStock());
    response.setAllocatedAmount(pharmacyDrug.getAllocatedAmount());
    return response;
  }

  /**
   * Creates a new prescription for a patient at a specific pharmacy.
   *
   * @param pharmacyId The ID of the pharmacy
   * @param request    The prescription request containing patient and drug information
   * @return A PrescriptionResponse containing the created prescription details
   * @throws ResourceNotFoundException If the pharmacy or patient is not found
   * @throws IllegalArgumentException  If the requested drugs are not contracted or quantities are
   *                                   invalid
   */
  @Override
  @Transactional
  public PrescriptionResponse createPrescription(Long pharmacyId, PrescriptionRequest request) {
    // Validate pharmacy exists
    Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Pharmacy not found with id: " + pharmacyId));

    // Validate patient exists
    Patient patient = patientRepository.findById(request.getPatientId())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Patient not found with id: " + request.getPatientId()));

    // Create a map of pharmacy drugs for easy lookup
    Map<Long, PharmacyDrug> pharmacyDrugMap = pharmacy.getPharmacyDrugs().stream()
        .collect(Collectors.toMap(pd -> pd.getDrug().getId(), Function.identity()));

    // Validate all drugs are contracted with the pharmacy
    for (PrescriptionDrugRequest drugRequest : request.getDrugs()) {
      if (!pharmacyDrugMap.containsKey(drugRequest.getDrugId())) {
        throw new IllegalArgumentException("Drug with ID "
            + drugRequest.getDrugId()
            + " is not contracted with this pharmacy");
      }

      PharmacyDrug pharmacyDrug = pharmacyDrugMap.get(drugRequest.getDrugId());

      // Validate drug is within allocation limits
      if (drugRequest.getQuantity() > pharmacyDrug.getAllocatedAmount()) {
        throw new IllegalArgumentException(
            "Requested quantity exceeds pharmacy's allocation for drug: "
                + pharmacyDrug.getDrug().getName());
      }

      // Validate drug is available in stock
      if (drugRequest.getQuantity() > pharmacyDrug.getDrug().getStock()) {
        throw new IllegalArgumentException("Requested quantity exceeds available stock for drug: "
            + pharmacyDrug.getDrug().getName());
      }
    }

    // Create prescription
    Prescription prescription = new Prescription();
    prescription.setPharmacy(pharmacy);
    prescription.setPatient(patient);
    prescription.setCreatedAt(LocalDateTime.now());
    prescription.setStatus(Prescription.PrescriptionStatus.CREATED);

    // Create prescription items
    Set<PrescriptionItem> items = request.getDrugs().stream()
        .map(drugRequest -> {
          PrescriptionItem item = new PrescriptionItem();
          item.setPrescription(prescription);
          item.setDrug(pharmacyDrugMap.get(drugRequest.getDrugId()).getDrug());
          item.setQuantity(drugRequest.getQuantity());
          item.setDosage(drugRequest.getDosage());
          return item;
        })
        .collect(Collectors.toSet());

    prescription.setItems(items);

    // Save prescription
    Prescription savedPrescription = prescriptionRepository.save(prescription);

    // Map to response
    return mapToPrescriptionResponse(savedPrescription);
  }

  /**
   * Maps a Prescription entity to a PrescriptionResponse DTO.
   *
   * @param prescription The prescription entity to map
   * @return A PrescriptionResponse containing the prescription details
   */
  private PrescriptionResponse mapToPrescriptionResponse(Prescription prescription) {
    PrescriptionResponse response = new PrescriptionResponse();
    response.setId(prescription.getId());
    response.setPharmacyId(prescription.getPharmacy().getId());
    response.setPharmacyName(prescription.getPharmacy().getName());
    response.setPatientId(prescription.getPatient().getId());
    response.setPatientName(prescription.getPatient().getName());
    response.setCreatedAt(prescription.getCreatedAt());
    response.setStatus(prescription.getStatus().name());

    List<PrescriptionDrugResponse> drugs = new ArrayList<>();
    for (PrescriptionItem item : prescription.getItems()) {
      PrescriptionDrugResponse drugResponse = new PrescriptionDrugResponse();
      drugResponse.setDrugId(item.getDrug().getId());
      drugResponse.setName(item.getDrug().getName());
      drugResponse.setManufacturer(item.getDrug().getManufacturer());
      drugResponse.setBatchNumber(item.getDrug().getBatchNumber());
      drugResponse.setQuantity(item.getQuantity());
      drugs.add(drugResponse);
    }

    response.setDrugs(drugs);
    return response;
  }

  /**
   * Fulfills a prescription by dispensing the requested drugs.
   *
   * @param prescriptionId The ID of the prescription to fulfill
   * @return A PrescriptionResponse containing the updated prescription details
   * @throws ResourceNotFoundException If the prescription is not found
   * @throws IllegalStateException     If the prescription cannot be fulfilled
   */
  @Transactional
  public PrescriptionResponse fulfillPrescription(Long prescriptionId) {
    try {
      return doFulfillPrescription(prescriptionId);
    } catch (IllegalStateException e) {
      // Re-throw the exception to be caught by the aspect
      throw e;
    }
  }

  /**
   * Internal method to fulfill a prescription in a new transaction.
   *
   * @param prescriptionId The ID of the prescription to fulfill
   * @return A PrescriptionResponse containing the updated prescription details
   * @throws ResourceNotFoundException If the prescription is not found
   * @throws IllegalStateException     If the prescription cannot be fulfilled
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  protected PrescriptionResponse doFulfillPrescription(Long prescriptionId) {
    // Find prescription with pessimistic lock to prevent concurrent modifications
    Prescription prescription = prescriptionRepository.findById(prescriptionId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Prescription not found with id: " + prescriptionId));

    // Validate prescription status
    if (prescription.getStatus() != Prescription.PrescriptionStatus.CREATED) {
      throw new IllegalStateException("Prescription has already been fulfilled or cancelled");
    }

    // Check each drug's expiry and stock
    for (PrescriptionItem item : prescription.getItems()) {
      Drug drug = item.getDrug();

      // Check expiry
      if (drug.getExpiryDate().isBefore(LocalDate.now())) {
        throw new IllegalStateException("Drug " + drug.getName() + " has expired");
      }

      // Check stock
      if (drug.getStock() < item.getQuantity()) {
        throw new IllegalStateException("Insufficient stock for drug: " + drug.getName());
      }

      // Reduce stock
      drug.setStock(drug.getStock() - item.getQuantity());

      // Save the updated drug stock and pharmacy drug allocation
      drugRepository.save(drug);
    }

    // Update prescription status
    prescription.setStatus(Prescription.PrescriptionStatus.FULFILLED);

    // Save changes
    Prescription savedPrescription = prescriptionRepository.save(prescription);

    // Return response
    return mapToPrescriptionResponse(savedPrescription);
  }
}