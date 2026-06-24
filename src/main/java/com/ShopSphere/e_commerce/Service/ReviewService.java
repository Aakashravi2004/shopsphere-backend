package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.ReviewRequestDto;
import com.ShopSphere.e_commerce.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    ReviewResponseDto addReview(Long productId, ReviewRequestDto reviewRequestDto);
    List<ReviewResponseDto> getReviewsByProduct(Long productId);
}
