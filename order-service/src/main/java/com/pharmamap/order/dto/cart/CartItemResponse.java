package com.pharmamap.order.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long id;
    private Long medicineId;
    private String medicineName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
