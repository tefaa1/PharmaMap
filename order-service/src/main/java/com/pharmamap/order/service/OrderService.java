package com.pharmamap.order.service;

import com.pharmamap.order.dto.order.CheckoutRequest;
import com.pharmamap.order.dto.order.OrderResponse;
import com.pharmamap.order.dto.order.UpdateOrderStatusRequest;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(CheckoutRequest request, String userId);

    OrderResponse getOrderById(Long orderId, String userId);

    List<OrderResponse> getUserOrders(String userId);

    List<OrderResponse> getPharmacyOrders(Long pharmacyId);

    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request, Long pharmacyId);
}
