package com.pharmamap.repository;

import com.pharmamap.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.medicineSet WHERE c.id = :categoryId")
    Optional<Category> findCategoryByIdWithMedicines(@Param("categoryId") Long categoryId);
}
