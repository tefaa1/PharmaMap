package com.pharmamap.order.repository;

import com.pharmamap.order.entity.Order;
import com.pharmamap.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserIdWithItems(@Param("userId") String userId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.pharmacyId = :pharmacyId ORDER BY o.createdAt DESC")
    List<Order> findByPharmacyIdWithItems(@Param("pharmacyId") Long pharmacyId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    List<Order> findByStatus(OrderStatus status);
}
