package com.pharmamap.order.service;

import com.pharmamap.order.dto.cart.AddToCartRequest;
import com.pharmamap.order.dto.cart.CartResponse;
import com.pharmamap.order.dto.cart.UpdateCartItemRequest;

public interface CartService {

    CartResponse addToCart(AddToCartRequest request, String userId);

    CartResponse getCart(String userId, Long pharmacyId);

    CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request, String userId);

    void removeFromCart(Long cartItemId, String userId);

    void clearCart(String userId, Long pharmacyId);
}
