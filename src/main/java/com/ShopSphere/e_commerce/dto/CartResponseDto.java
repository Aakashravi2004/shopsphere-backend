package com.ShopSphere.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {

    private Long cartId;
    private Long userId;
    private Integer totalItems;
    private Double totalPrice;
    private List<CartItemResponseDto> items;

}
