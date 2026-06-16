package com.ShopSphere.e_commerce.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartItemRequestDto {

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;

}
