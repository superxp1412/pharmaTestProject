package com.pei.pharmatest.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pei.pharmatest.dto.ContractedDrugResponse;
import com.pei.pharmatest.dto.PharmacyResponse;
import com.pei.pharmatest.dto.PrescriptionDrugRequest;
import com.pei.pharmatest.dto.PrescriptionDrugResponse;
import com.pei.pharmatest.dto.PrescriptionRequest;
import com.pei.pharmatest.dto.PrescriptionResponse;
import com.pei.pharmatest.exceptions.GlobalExceptionHandler;
import com.pei.pharmatest.exceptions.ResourceNotFoundException;
import com.pei.pharmatest.services.PharmacyService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PharmacyControllerTest {

  @Mock
  private PharmacyService pharmacyService;

  @InjectMocks
  private PharmacyController pharmacyController;

  private MockMvc mockMvc;

  @Test
  void getAllPharmacies_ShouldReturnPharmaciesList() throws Exception {
    // Given
    PharmacyResponse pharmacy1 = new PharmacyResponse();
    pharmacy1.setId(1L);
    pharmacy1.setName("Pharmacy One");
    pharmacy1.setAddress("123 Main St");

    ContractedDrugResponse drug1 = new ContractedDrugResponse();
    drug1.setId(1L);
    drug1.setName("Drug A");
    drug1.setManufacturer("Manufacturer A");
    drug1.setBatchNumber("BATCH001");
    drug1.setExpiryDate(LocalDate.now().plusMonths(6));
    drug1.setStock(100);
    drug1.setAllocatedAmount(50);

    pharmacy1.setContractedDrugs(new HashSet<>(Arrays.asList(drug1)));

    when(pharmacyService.getAllPharmacies()).thenReturn(List.of(pharmacy1));

    mockMvc = MockMvcBuilders.standaloneSetup(pharmacyController).build();

    // When & Then
    mockMvc.perform(get("/api/v1/pharmacies")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Pharmacy One"))
        .andExpect(jsonPath("$[0].address").value("123 Main St"))
        .andExpect(jsonPath("$[0].contractedDrugs[0].name").value("Drug A"))
        .andExpect(jsonPath("$[0].contractedDrugs[0].allocatedAmount").value(50));
  }

  @Test
  void getAllPharmacies_WhenNoPharmacies_ShouldReturnEmptyList() throws Exception {
    // Given
    when(pharmacyService.getAllPharmacies()).thenReturn(List.of());

    mockMvc = MockMvcBuilders.standaloneSetup(pharmacyController).build();

    // When & Then
    mockMvc.perform(get("/api/v1/pharmacies")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void createPrescription_ShouldCreatePrescription_WhenValidRequest() throws Exception {
    // Given
    Long pharmacyId = 1L;
    PrescriptionRequest request = new PrescriptionRequest();
    request.setPatientId(1L);

    PrescriptionDrugRequest drugRequest = new PrescriptionDrugRequest();
    drugRequest.setDrugId(1L);
    drugRequest.setQuantity(10);
    request.setDrugs(List.of(drugRequest));

    PrescriptionResponse expectedResponse = new PrescriptionResponse();
    expectedResponse.setId(1L);
    expectedResponse.setPharmacyId(pharmacyId);
    expectedResponse.setPharmacyName("Pharmacy One");
    expectedResponse.setPatientId(1L);
    expectedResponse.setPatientName("John Doe");
    expectedResponse.setCreatedAt(LocalDateTime.now());
    expectedResponse.setStatus("CREATED");

    PrescriptionDrugResponse drugResponse = new PrescriptionDrugResponse();
    drugResponse.setDrugId(1L);
    drugResponse.setName("Drug A");
    drugResponse.setManufacturer("Manufacturer A");
    drugResponse.setBatchNumber("BATCH001");
    drugResponse.setQuantity(10);
    expectedResponse.setDrugs(List.of(drugResponse));

    when(pharmacyService.createPrescription(eq(pharmacyId), any(PrescriptionRequest.class)))
        .thenReturn(expectedResponse);

    mockMvc = MockMvcBuilders.standaloneSetup(pharmacyController).build();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules(); // For LocalDateTime serialization

    // When & Then
    mockMvc.perform(post("/api/v1/pharmacies/{pharmacyId}/prescriptions", pharmacyId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.pharmacyId").value(pharmacyId))
        .andExpect(jsonPath("$.pharmacyName").value("Pharmacy One"))
        .andExpect(jsonPath("$.patientId").value(1))
        .andExpect(jsonPath("$.patientName").value("John Doe"))
        .andExpect(jsonPath("$.status").value("CREATED"))
        .andExpect(jsonPath("$.drugs[0].drugId").value(1))
        .andExpect(jsonPath("$.drugs[0].name").value("Drug A"))
        .andExpect(jsonPath("$.drugs[0].quantity").value(10));
  }

  @Test
  void createPrescription_ShouldReturnNotFound_WhenPharmacyNotFound() throws Exception {
    // Given
    Long pharmacyId = 999L;
    PrescriptionRequest request = new PrescriptionRequest();
    request.setPatientId(1L);

    PrescriptionDrugRequest drugRequest = new PrescriptionDrugRequest();
    drugRequest.setDrugId(1L);
    drugRequest.setQuantity(10);
    request.setDrugs(List.of(drugRequest));

    when(pharmacyService.createPrescription(eq(pharmacyId), any(PrescriptionRequest.class)))
        .thenThrow(new ResourceNotFoundException("Pharmacy not found with id: " + pharmacyId));

    mockMvc = MockMvcBuilders.standaloneSetup(pharmacyController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
    ObjectMapper objectMapper = new ObjectMapper();

    // When & Then
    mockMvc.perform(post("/api/v1/pharmacies/{pharmacyId}/prescriptions", pharmacyId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Resource not found"))
        .andExpect(jsonPath("$.message").value("Pharmacy not found with id: " + pharmacyId));
  }

  @Test
  void createPrescription_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
    // Given
    Long pharmacyId = 1L;
    PrescriptionRequest request = new PrescriptionRequest();
    request.setPatientId(1L);

    PrescriptionDrugRequest drugRequest = new PrescriptionDrugRequest();
    drugRequest.setDrugId(4L); // Non-contracted drug
    drugRequest.setQuantity(10);
    request.setDrugs(List.of(drugRequest));

    String errorMessage = "Drug with ID 4 is not contracted with this pharmacy";
    when(pharmacyService.createPrescription(eq(pharmacyId), any(PrescriptionRequest.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    mockMvc = MockMvcBuilders.standaloneSetup(pharmacyController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
    ObjectMapper objectMapper = new ObjectMapper();

    // When & Then
    mockMvc.perform(post("/api/v1/pharmacies/{pharmacyId}/prescriptions", pharmacyId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid input"))
        .andExpect(jsonPath("$.message").value(errorMessage));
  }
}