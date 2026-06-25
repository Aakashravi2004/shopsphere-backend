package com.ShopSphere.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDto {

    private Long  wishlistId;
    private Long productId;
    private String productName;
    private Double price;
    private String imageUrl;
    private Double averageRating;
    private LocalDateTime addedAt;
}
