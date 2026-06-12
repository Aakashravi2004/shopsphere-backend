package com.ShopSphere.e_commerce.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Positive
    private Double price;
    @NotNull
    @PositiveOrZero
    private Integer stockQuantity;
    @NotBlank
    private String imageUrl;
    @NotNull
    private Long categoryId;

}
