package com.pharmamap.order.service;

import com.pharmamap.order.dto.cart.AddToCartRequest;
import com.pharmamap.order.dto.cart.CartResponse;
import com.pharmamap.order.dto.cart.UpdateCartItemRequest;
import com.pharmamap.order.entity.Cart;
import com.pharmamap.order.entity.CartItem;
import com.pharmamap.order.mapper.CartMapper;
import com.pharmamap.order.repository.CartItemRepository;
import com.pharmamap.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse addToCart(AddToCartRequest request, String userId) {
        // Find or create cart for user and pharmacy
        Cart cart = cartRepository.findByUserIdAndPharmacyIdWithItems(userId, request.getPharmacyId())
                .orElseGet(() -> Cart.builder()
                        .userId(userId)
                        .pharmacyId(request.getPharmacyId())
                        .build());

        // Check if medicine already exists in cart
        CartItem existingItem = cartItemRepository.findByCartIdAndMedicineId(cart.getId(), request.getMedicineId())
                .orElse(null);

        if (existingItem != null) {
            // Update quantity
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            // Create new cart item
            CartItem cartItem = CartItem.builder()
                    .medicineId(request.getMedicineId())
                    .medicineName("Medicine Name") // TODO: Fetch from pharmacy service
                    .price(10.0) // TODO: Fetch from pharmacy service
                    .quantity(request.getQuantity())
                    .cart(cart)
                    .build();
            cart.addCartItem(cartItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toResponse(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String userId, Long pharmacyId) {
        Cart cart = cartRepository.findByUserIdAndPharmacyIdWithItems(userId, pharmacyId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request, String userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Verify ownership
        if (!cartItem.getCart().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        Cart cart = cartRepository.findByUserIdAndPharmacyIdWithItems(userId, cartItem.getCart().getPharmacyId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartMapper.toResponse(cart);
    }

    @Override
    public void removeFromCart(Long cartItemId, String userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Verify ownership
        if (!cartItem.getCart().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId, Long pharmacyId) {
        Cart cart = cartRepository.findByUserIdAndPharmacyIdWithItems(userId, pharmacyId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
