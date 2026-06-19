package com.ShopSphere.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double subtotal;

}
