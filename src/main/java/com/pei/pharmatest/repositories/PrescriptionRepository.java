package com.pei.pharmatest.repositories;

import com.pei.pharmatest.entities.Prescription;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Prescription p LEFT JOIN FETCH p.items WHERE p.id = :id")
  Optional<Prescription> findById(@Param("id") Long id);
}