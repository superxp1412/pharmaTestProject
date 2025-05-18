package com.pei.pharmatest.aspects;

import java.util.Collections;
import com.pei.pharmatest.dto.PrescriptionRequest;
import com.pei.pharmatest.dto.PrescriptionResponse;
import com.pei.pharmatest.entities.AuditLog;
import com.pei.pharmatest.exceptions.ResourceNotFoundException;
import com.pei.pharmatest.repositories.AuditLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Aspect for handling audit logging of prescription-related operations. This aspect intercepts
 * prescription creation and fulfillment operations to maintain an audit trail of all
 * prescription-related activities.
 */
@Aspect
@Component
public class AuditLogAspect {

  private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);
  private final AuditLogRepository auditLogRepository;
  private final PlatformTransactionManager transactionManager;

  /**
   * Constructs a new AuditLogAspect with the required dependencies.
   *
   * @param auditLogRepository The repository for saving audit logs
   * @param transactionManager The transaction manager for managing audit log transactions
   */
  public AuditLogAspect(AuditLogRepository auditLogRepository,
      PlatformTransactionManager transactionManager) {
    this.auditLogRepository = auditLogRepository;
    this.transactionManager = transactionManager;
  }

  private void handleAuditLogFailure(AuditLog log, Exception e, String errorType) {
    log.setStatus("FAILURE");
    log.setFailureReason(errorType + ": " + e.getMessage());
    if (log.getPatientId() == null)
      log.setPatientId(-1L);
    if (log.getPharmacyId() == null)
      log.setPharmacyId(-1L);
    if (log.getPrescriptionId() == null)
      log.setPrescriptionId(-1L);
    saveAuditLog(log);
  }

  /**
   * Intercepts prescription creation operations and logs the audit information.
   *
   * @param joinPoint The join point representing the intercepted method
   * @return The result of the intercepted method
   * @throws Throwable If an error occurs during the operation
   */
  @Around("execution(* com.pei.pharmatest.services.impl.PharmacyServiceImpl.createPrescription(..))")
  public Object logCreatePrescription(ProceedingJoinPoint joinPoint) throws Throwable {
    Long pharmacyId = (Long) joinPoint.getArgs()[0];
    PrescriptionRequest request = (PrescriptionRequest) joinPoint.getArgs()[1];
    AuditLog log = new AuditLog();
    log.setPharmacyId(pharmacyId);
    log.setPatientId(request.getPatientId());
    log.setDrugsRequested(request.getDrugs());
    log.setDrugsDispensed(Collections.emptyList());

    try {
      PrescriptionResponse response = (PrescriptionResponse) joinPoint.proceed();
      log.setPrescriptionId(response.getId());
      log.setDrugsDispensed(response.getDrugs());
      log.setStatus("SUCCESS");
      saveAuditLog(log);
      return response;
    } catch (ResourceNotFoundException e) {
      handleAuditLogFailure(log, e, "Resource not found");
      throw e;
    } catch (IllegalArgumentException e) {
      handleAuditLogFailure(log, e, "Invalid input");
      throw e;
    } catch (Exception e) {
      handleAuditLogFailure(log, e, "Unexpected error");
      throw e;
    }
  }

  /**
   * Intercepts prescription fulfillment operations and logs the audit information.
   *
   * @param joinPoint The join point representing the intercepted method
   * @return The result of the intercepted method
   * @throws Throwable If an error occurs during the operation
   */
  @Around("execution(* com.pei.pharmatest.services.impl.PharmacyServiceImpl.fulfillPrescription(..))")
  public Object logFulfillPrescription(ProceedingJoinPoint joinPoint) throws Throwable {
    Long prescriptionId = (Long) joinPoint.getArgs()[0];
    AuditLog log = new AuditLog();
    log.setPrescriptionId(prescriptionId);
    log.setDrugsRequested(Collections.emptyList());
    log.setDrugsDispensed(Collections.emptyList());

    try {
      PrescriptionResponse response = (PrescriptionResponse) joinPoint.proceed();
      log.setPatientId(response.getPatientId());
      log.setPharmacyId(response.getPharmacyId());
      log.setDrugsDispensed(response.getDrugs());
      log.setStatus("SUCCESS");
      saveAuditLog(log);
      return response;
    } catch (ResourceNotFoundException e) {
      handleAuditLogFailure(log, e, "Resource not found");
      throw e;
    } catch (IllegalStateException e) {
      handleAuditLogFailure(log, e, "Invalid state");
      throw e;
    } catch (Exception e) {
      handleAuditLogFailure(log, e, "Unexpected error");
      throw e;
    }
  }

  /**
   * Saves the audit log in a new transaction.
   *
   * @param log The audit log to be saved
   */
  protected void saveAuditLog(AuditLog log) {
    TransactionStatus status = null;
    try {
      DefaultTransactionDefinition def = new DefaultTransactionDefinition();
      def.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
      status = transactionManager.getTransaction(def);

      auditLogRepository.save(log);

      transactionManager.commit(status);
    } catch (Exception e) {
      if (status != null) {
        transactionManager.rollback(status);
      }
      logger.error("Failed to save audit log: {}", e.getMessage(), e);
    }
  }
}
