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
public class ReviewResponseDto {

    private Long reviewId;
    private Integer rating;
    private String comment;
    private String userName;
    private Long productId;
    private LocalDateTime createdDate;

}
