package com.pharmamap.order.mapper;

import com.pharmamap.order.dto.order.OrderItemResponse;
import com.pharmamap.order.dto.order.OrderResponse;
import com.pharmamap.order.entity.Order;
import com.pharmamap.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponse toResponse(Order order);

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems);
}
