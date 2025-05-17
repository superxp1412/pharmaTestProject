package com.pei.pharmatest.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pei.pharmatest.dto.DrugRequest;
import com.pei.pharmatest.dto.DrugResponse;
import com.pei.pharmatest.exceptions.GlobalExceptionHandler;
import com.pei.pharmatest.services.DrugService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class DrugControllerTest {

  @Mock
  private DrugService drugService;

  @InjectMocks
  private DrugController drugController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(drugController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
    objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
  }

  @Test
  void getDrug_ShouldReturnDrug_WhenDrugExists() throws Exception {
    // Given
    Long drugId = 1L;
    DrugResponse drugResponse = new DrugResponse();
    drugResponse.setId(drugId);
    drugResponse.setName("Aspirin");
    drugResponse.setManufacturer("Bayer");
    drugResponse.setBatchNumber("BATCH123");
    drugResponse.setExpiryDate(LocalDate.now().plusYears(2));
    drugResponse.setStock(100);
    drugResponse.setCreatedAt(LocalDateTime.now());

    when(drugService.getDrug(drugId)).thenReturn(Optional.of(drugResponse));

    // When & Then
    mockMvc.perform(get("/api/v1/drugs/{id}", drugId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(drugId))
        .andExpect(jsonPath("$.name").value("Aspirin"))
        .andExpect(jsonPath("$.manufacturer").value("Bayer"))
        .andExpect(jsonPath("$.batchNumber").value("BATCH123"))
        .andExpect(jsonPath("$.stock").value(100));
  }

  @Test
  void getDrug_ShouldReturnNotFound_WhenDrugDoesNotExist() throws Exception {
    // Given
    Long drugId = 999L;
    when(drugService.getDrug(drugId)).thenReturn(Optional.empty());

    // When & Then
    mockMvc.perform(get("/api/v1/drugs/{id}", drugId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("Drug not found"))
        .andExpect(jsonPath("$.message").value(
            String.format("Drug with ID %d does not exist in the inventory", drugId)));
  }

  @Test
  void getDrug_ShouldReturnBadRequest_WhenInvalidId() throws Exception {
    // Given
    Long invalidId = -1L;

    // When & Then
    mockMvc.perform(get("/api/v1/drugs/{id}", invalidId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid input"))
        .andExpect(jsonPath("$.message").value("Drug ID must be a positive number"));
  }

  @Test
  void addDrug_ShouldCreateDrug_WhenValidRequest() throws Exception {
    // Given
    DrugRequest request = new DrugRequest();
    request.setName("Aspirin");
    request.setManufacturer("Bayer");
    request.setBatchNumber("BATCH123");
    request.setExpiryDate(LocalDate.now().plusYears(2));
    request.setStock(100);

    DrugResponse expectedResponse = new DrugResponse();
    expectedResponse.setId(1L);
    expectedResponse.setName(request.getName());
    expectedResponse.setManufacturer(request.getManufacturer());
    expectedResponse.setBatchNumber(request.getBatchNumber());
    expectedResponse.setExpiryDate(request.getExpiryDate());
    expectedResponse.setStock(request.getStock());
    expectedResponse.setCreatedAt(LocalDateTime.now());

    when(drugService.addDrug(any(DrugRequest.class))).thenReturn(expectedResponse);

    // When & Then
    mockMvc.perform(post("/api/v1/drugs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Aspirin"))
        .andExpect(jsonPath("$.manufacturer").value("Bayer"))
        .andExpect(jsonPath("$.batchNumber").value("BATCH123"))
        .andExpect(jsonPath("$.stock").value(100));
  }

  @Test
  void addDrug_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
    // Given
    DrugRequest request = new DrugRequest();
    String errorMessage = "Drug name is required";
    when(drugService.addDrug(any(DrugRequest.class)))
        .thenThrow(new IllegalArgumentException(errorMessage));

    // When & Then
    mockMvc.perform(post("/api/v1/drugs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid input"))
        .andExpect(jsonPath("$.message").value(errorMessage));
  }
}