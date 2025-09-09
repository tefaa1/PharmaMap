package com.pharmamap.order.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {

    @NotNull
    private Long pharmacyId;

    @NotNull
    private Long medicineId;

    @NotNull
    @Positive
    private Integer quantity;
}
