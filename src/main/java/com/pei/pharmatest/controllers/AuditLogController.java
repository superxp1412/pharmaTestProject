package com.pei.pharmatest.controllers;

import com.pei.pharmatest.entities.AuditLog;
import com.pei.pharmatest.services.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long pharmacyId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<AuditLog> auditLogs = auditLogService.getAuditLogs(
            patientId,
            pharmacyId, 
            status, 
            PageRequest.of(page, size)
        );
        
        return ResponseEntity.ok(auditLogs);
    }
}