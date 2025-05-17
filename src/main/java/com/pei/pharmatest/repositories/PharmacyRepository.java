package com.pei.pharmatest.repositories;

import com.pei.pharmatest.entities.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

}