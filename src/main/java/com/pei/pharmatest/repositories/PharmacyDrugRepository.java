package com.pei.pharmatest.repositories;

import com.pei.pharmatest.entities.PharmacyDrug;
import com.pei.pharmatest.entities.PharmacyDrugId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyDrugRepository extends JpaRepository<PharmacyDrug, PharmacyDrugId> {

}