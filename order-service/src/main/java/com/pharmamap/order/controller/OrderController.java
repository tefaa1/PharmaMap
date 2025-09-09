package com.pharmamap.order.controller;

import com.pharmamap.order.dto.order.CheckoutRequest;
import com.pharmamap.order.dto.order.OrderResponse;
import com.pharmamap.order.dto.order.UpdateOrderStatusRequest;
import com.pharmamap.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request,
                                                 @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.checkout(request, userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId,
                                                @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId, userId));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<List<OrderResponse>> getPharmacyOrders(@PathVariable Long pharmacyId,
                                                               @RequestHeader("X-Pharmacy-Id") Long pharmacyIdHeader) {
        // Verify pharmacy access
        if (!pharmacyId.equals(pharmacyIdHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(orderService.getPharmacyOrders(pharmacyId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId,
                                                         @Valid @RequestBody UpdateOrderStatusRequest request,
                                                         @RequestHeader("X-Pharmacy-Id") Long pharmacyId) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request, pharmacyId));
    }
}
