package com.pei.pharmatest.repositories;

import com.pei.pharmatest.entities.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug, Long> {

}