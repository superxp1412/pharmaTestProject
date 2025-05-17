package com.pei.pharmatest.services.impl;

import com.pei.pharmatest.entities.AuditLog;
import com.pei.pharmatest.repositories.AuditLogRepository;
import com.pei.pharmatest.services.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public Page<AuditLog> getAuditLogs(Long patientId, Long pharmacyId, String status, Pageable pageable) {
        Specification<AuditLog> spec = Specification.where(null);

        if (patientId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("patientId"), patientId));
        }

        if (pharmacyId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("pharmacyId"), pharmacyId));
        }

        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        return auditLogRepository.findAll(spec, pageable);
    }
}