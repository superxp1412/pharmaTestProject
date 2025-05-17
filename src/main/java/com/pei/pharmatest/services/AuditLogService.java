package com.pei.pharmatest.services;

import com.pei.pharmatest.entities.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    Page<AuditLog> getAuditLogs(Long patientId, Long pharmacyId, String status, Pageable pageable);
}