package com.pei.pharmatest.repositories;

import com.pei.pharmatest.entities.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {

}