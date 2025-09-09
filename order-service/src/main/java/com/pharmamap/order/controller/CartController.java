package com.pharmamap.order.controller;

import com.pharmamap.order.dto.cart.AddToCartRequest;
import com.pharmamap.order.dto.cart.CartResponse;
import com.pharmamap.order.dto.cart.UpdateCartItemRequest;
import com.pharmamap.order.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody AddToCartRequest request,
                                                  @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addToCart(request, userId));
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long pharmacyId,
                                               @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId, pharmacyId));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable Long cartItemId,
                                                      @Valid @RequestBody UpdateCartItemRequest request,
                                                      @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(cartService.updateCartItem(cartItemId, request, userId));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId,
                                              @RequestHeader("X-User-Id") String userId) {
        cartService.removeFromCart(cartItemId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long pharmacyId,
                                         @RequestHeader("X-User-Id") String userId) {
        cartService.clearCart(userId, pharmacyId);
        return ResponseEntity.noContent().build();
    }
}
