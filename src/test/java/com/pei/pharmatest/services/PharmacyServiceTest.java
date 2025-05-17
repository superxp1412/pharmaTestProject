package com.pei.pharmatest.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.pei.pharmatest.dto.ContractedDrugResponse;
import com.pei.pharmatest.dto.PharmacyResponse;
import com.pei.pharmatest.entities.Drug;
import com.pei.pharmatest.entities.Pharmacy;
import com.pei.pharmatest.entities.PharmacyDrug;
import com.pei.pharmatest.entities.PharmacyDrugId;
import com.pei.pharmatest.repositories.PharmacyRepository;
import com.pei.pharmatest.services.impl.PharmacyServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PharmacyServiceTest {

  @Mock
  private PharmacyRepository pharmacyRepository;

  @InjectMocks
  private PharmacyServiceImpl pharmacyService;

  private Pharmacy pharmacy1;
  private Pharmacy pharmacy2;
  private Drug drug1;
  private Drug drug2;

  @BeforeEach
  void setUp() {
    // Setup test data
    pharmacy1 = new Pharmacy();
    pharmacy1.setId(1L);
    pharmacy1.setName("Pharmacy One");
    pharmacy1.setAddress("123 Main St");

    pharmacy2 = new Pharmacy();
    pharmacy2.setId(2L);
    pharmacy2.setName("Pharmacy Two");
    pharmacy2.setAddress("456 Oak St");

    drug1 = new Drug();
    drug1.setId(1L);
    drug1.setName("Drug A");
    drug1.setManufacturer("Manufacturer A");
    drug1.setBatchNumber("BATCH001");
    drug1.setExpiryDate(LocalDate.now().plusMonths(6));
    drug1.setStock(100);

    drug2 = new Drug();
    drug2.setId(2L);
    drug2.setName("Drug B");
    drug2.setManufacturer("Manufacturer B");
    drug2.setBatchNumber("BATCH002");
    drug2.setExpiryDate(LocalDate.now().plusMonths(12));
    drug2.setStock(200);

    // Setup pharmacy-drug relationships
    PharmacyDrug pd1 = new PharmacyDrug();
    PharmacyDrugId id1 = new PharmacyDrugId();
    id1.setPharmacyId(1L);
    id1.setDrugId(1L);
    pd1.setId(id1);
    pd1.setPharmacy(pharmacy1);
    pd1.setDrug(drug1);
    pd1.setAllocatedAmount(50);

    PharmacyDrug pd2 = new PharmacyDrug();
    PharmacyDrugId id2 = new PharmacyDrugId();
    id2.setPharmacyId(1L);
    id2.setDrugId(2L);
    pd2.setId(id2);
    pd2.setPharmacy(pharmacy1);
    pd2.setDrug(drug2);
    pd2.setAllocatedAmount(100);

    pharmacy1.setPharmacyDrugs(new HashSet<>(Arrays.asList(pd1, pd2)));
  }

  @Test
  void getAllPharmacies_ShouldReturnAllPharmaciesWithContractedDrugs() {
    // Given
    when(pharmacyRepository.findAll()).thenReturn(Arrays.asList(pharmacy1, pharmacy2));

    // When
    List<PharmacyResponse> result = pharmacyService.getAllPharmacies();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);

    PharmacyResponse firstPharmacy = result.get(0);
    assertThat(firstPharmacy.getId()).isEqualTo(1L);
    assertThat(firstPharmacy.getName()).isEqualTo("Pharmacy One");
    assertThat(firstPharmacy.getAddress()).isEqualTo("123 Main St");
    assertThat(firstPharmacy.getContractedDrugs()).hasSize(2);

    Set<ContractedDrugResponse> contractedDrugs = firstPharmacy.getContractedDrugs();
    ContractedDrugResponse firstDrug = contractedDrugs.stream()
        .filter(d -> d.getId().equals(1L))
        .findFirst()
        .orElseThrow();

    assertThat(firstDrug.getName()).isEqualTo("Drug A");
    assertThat(firstDrug.getAllocatedAmount()).isEqualTo(50);
    assertThat(firstDrug.getStock()).isEqualTo(100);
  }

  @Test
  void getAllPharmacies_WhenNoPharmacies_ShouldReturnEmptyList() {
    // Given
    when(pharmacyRepository.findAll()).thenReturn(List.of());

    // When
    List<PharmacyResponse> result = pharmacyService.getAllPharmacies();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();
  }
}