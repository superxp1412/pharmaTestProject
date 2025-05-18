package com.pei.pharmatest.services.impl;

import com.pei.pharmatest.entities.AuditLog;
import com.pei.pharmatest.exceptions.ValidationException;
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
    public Page<AuditLog> getAuditLogs(Long patientId, Long pharmacyId, String status,
            Pageable pageable) {
        validatePageable(pageable);
        validateStatus(status);

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

    private void validatePageable(Pageable pageable) {
        if (pageable == null) {
            throw new ValidationException("Pageable parameters cannot be null");
        }
        if (pageable.getPageSize() <= 0) {
            throw new ValidationException("Page size must be greater than zero");
        }
        if (pageable.getPageNumber() < 0) {
            throw new ValidationException("Page number cannot be negative");
        }
    }

    private void validateStatus(String status) {
        if (status != null && !status.isEmpty()) {
            if (!status.matches("^[A-Z_]+$")) {
                throw new ValidationException(
                        "Invalid status format. Status should be uppercase with underscores");
            }
        }
    }
}
