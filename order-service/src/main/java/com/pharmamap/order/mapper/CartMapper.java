package com.pharmamap.order.mapper;

import com.pharmamap.order.dto.cart.CartItemResponse;
import com.pharmamap.order.dto.cart.CartResponse;
import com.pharmamap.order.entity.Cart;
import com.pharmamap.order.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartMapper {

    @Mapping(target = "cartItems", source = "cartItems")
    @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(cart))")
    CartResponse toResponse(Cart cart);

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItems);

    default Double calculateTotalAmount(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
