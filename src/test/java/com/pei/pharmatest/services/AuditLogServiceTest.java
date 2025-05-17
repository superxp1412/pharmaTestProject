package com.pei.pharmatest.services;

import com.pei.pharmatest.entities.AuditLog;
import com.pei.pharmatest.repositories.AuditLogRepository;
import com.pei.pharmatest.services.impl.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuditLogServiceTest {

  @Mock
  private AuditLogRepository auditLogRepository;

  private AuditLogService auditLogService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    auditLogService = new AuditLogServiceImpl(auditLogRepository);
  }

  @Test
  void getAuditLogs_WithPatientFilter_ShouldReturnFilteredLogs() {
    // Given
    Long patientId = 1L;
    AuditLog log1 = createAuditLog(1L, patientId, 1L, "SUCCESS");
    AuditLog log2 = createAuditLog(2L, patientId, 2L, "FAILURE");
    List<AuditLog> logs = Arrays.asList(log1, log2);
    Page<AuditLog> page = new PageImpl<>(logs);

    when(auditLogRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);

    // When
    Page<AuditLog> result = auditLogService.getAuditLogs(patientId, null, null,
        PageRequest.of(0, 10));

    // Then
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    assertEquals(patientId, result.getContent().get(0).getPatientId());
  }

  @Test
  void getAuditLogs_WithPharmacyFilter_ShouldReturnFilteredLogs() {
    // Given
    Long pharmacyId = 1L;
    AuditLog log1 = createAuditLog(1L, 1L, pharmacyId, "SUCCESS");
    List<AuditLog> logs = List.of(log1);
    Page<AuditLog> page = new PageImpl<>(logs);

    when(auditLogRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);

    // When
    Page<AuditLog> result = auditLogService.getAuditLogs(null, pharmacyId, null,
        PageRequest.of(0, 10));

    // Then
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(pharmacyId, result.getContent().get(0).getPharmacyId());
  }

  @Test
  void getAuditLogs_WithStatusFilter_ShouldReturnFilteredLogs() {
    // Given
    String status = "SUCCESS";
    AuditLog log1 = createAuditLog(1L, 1L, 1L, status);
    List<AuditLog> logs = List.of(log1);
    Page<AuditLog> page = new PageImpl<>(logs);

    when(auditLogRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);

    // When
    Page<AuditLog> result = auditLogService.getAuditLogs(null, null, status, PageRequest.of(0, 10));

    // Then
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(status, result.getContent().get(0).getStatus());
  }

  private AuditLog createAuditLog(Long id, Long patientId, Long pharmacyId, String status) {
    AuditLog log = new AuditLog();
    log.setId(id);
    log.setPatientId(patientId);
    log.setPharmacyId(pharmacyId);
    log.setStatus(status);
    return log;
  }
}