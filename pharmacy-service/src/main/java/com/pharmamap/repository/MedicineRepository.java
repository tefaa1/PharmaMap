package com.pharmamap.repository;

import com.pharmamap.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long> {
	List<Medicine> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name);
}
