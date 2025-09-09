package com.pharmamap.order.repository;

import com.pharmamap.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.userId = :userId AND c.pharmacyId = :pharmacyId")
    Optional<Cart> findByUserIdAndPharmacyIdWithItems(@Param("userId") String userId, @Param("pharmacyId") Long pharmacyId);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.userId = :userId")
    Optional<Cart> findByUserIdWithItems(@Param("userId") String userId);
}
