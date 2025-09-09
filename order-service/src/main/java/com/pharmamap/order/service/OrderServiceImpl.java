package com.pharmamap.order.service;

import com.pharmamap.order.dto.order.CheckoutRequest;
import com.pharmamap.order.dto.order.OrderResponse;
import com.pharmamap.order.dto.order.UpdateOrderStatusRequest;
import com.pharmamap.order.entity.*;
import com.pharmamap.order.mapper.OrderMapper;
import com.pharmamap.order.repository.CartRepository;
import com.pharmamap.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse checkout(CheckoutRequest request, String userId) {
        // Get cart for user and pharmacy
        Cart cart = cartRepository.findByUserIdAndPharmacyIdWithItems(userId, request.getPharmacyId())
                .orElseThrow(() -> new RuntimeException("Cart not found or empty"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create order from cart
        Order order = Order.builder()
                .userId(userId)
                .pharmacyId(request.getPharmacyId())
                .pharmacyName("Pharmacy Name") // TODO: Fetch from pharmacy service
                .pharmacyAddress("Pharmacy Address") // TODO: Fetch from pharmacy service
                .pharmacyPhone("Pharmacy Phone") // TODO: Fetch from pharmacy service
                .status(OrderStatus.PENDING)
                .totalAmount(calculateTotalAmount(cart))
                .build();

        // Convert cart items to order items
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .medicineId(cartItem.getMedicineId())
                    .medicineName(cartItem.getMedicineName())
                    .price(cartItem.getPrice())
                    .quantity(cartItem.getQuantity())
                    .order(order)
                    .build();
            order.addOrderItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful checkout
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId, String userId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to order");
        }

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserIdWithItems(userId);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getPharmacyOrders(Long pharmacyId) {
        List<Order> orders = orderRepository.findByPharmacyIdWithItems(pharmacyId);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request, Long pharmacyId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getPharmacyId().equals(pharmacyId)) {
            throw new RuntimeException("Unauthorized access to order");
        }

        order.setStatus(request.getStatus());
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    private Double calculateTotalAmount(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
