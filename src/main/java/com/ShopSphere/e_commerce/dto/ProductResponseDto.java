package com.ShopSphere.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stockQuantity;

    private String imageUrl;

    private Long categoryId;

    private String categoryName;

    private Double averageRating;

}
