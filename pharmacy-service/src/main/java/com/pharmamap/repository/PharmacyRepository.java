package com.pharmamap.repository;

import com.pharmamap.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy,Long> {

    @Query("SELECT p FROM Pharmacy p LEFT JOIN FETCH p.categorySet WHERE p.id = :pharmacyId")
    Optional<Pharmacy> findPharmacyByIdWithCategories(@Param("pharmacyId") Long pharmacyId);

    List<Pharmacy>findByUserId(String userID);
}
