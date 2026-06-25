package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.Review;
import com.ShopSphere.e_commerce.Repository.ReviewRepository;
import com.ShopSphere.e_commerce.Service.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final ReviewRepository reviewRepository;

    public RatingServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Double calculateAverageRating(Long productId){
        List<Review> reviews = reviewRepository.findByProductId(productId);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return (double) sum / reviews.size();

//        Sort code using Stream API Java 8 Features
//        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

}
