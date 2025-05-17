package com.pei.pharmatest.repositories;

import com.pei.pharmatest.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Patient entity operations. Provides data access operations for the
 * Patient entity.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
  // Spring Data JPA will automatically implement basic CRUD operations
  // Additional custom query methods can be added here if needed
}