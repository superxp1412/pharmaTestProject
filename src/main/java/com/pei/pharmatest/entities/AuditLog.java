package com.pei.pharmatest.entities;

import com.pei.pharmatest.dto.PrescriptionDrugRequest;
import com.pei.pharmatest.dto.PrescriptionDrugResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long prescriptionId;
  private Long patientId;
  private Long pharmacyId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<PrescriptionDrugRequest> drugsRequested;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<PrescriptionDrugResponse> drugsDispensed;

  private String failureReason;
  private String status;

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();
}