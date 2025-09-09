package com.pharmamap.order.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long id;
    private Long medicineId;
    private String medicineName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
