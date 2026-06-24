package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Service.ReviewService;
import com.ShopSphere.e_commerce.dto.ReviewRequestDto;
import com.ShopSphere.e_commerce.dto.ReviewResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewResponseDto> addReview(@PathVariable Long productId, @Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        return ResponseEntity.ok(reviewService.addReview(productId, reviewRequestDto));
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

}
