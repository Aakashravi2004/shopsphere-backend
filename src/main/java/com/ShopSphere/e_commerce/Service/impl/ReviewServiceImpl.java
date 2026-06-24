package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.*;
import com.ShopSphere.e_commerce.Enum.OrderStatus;
import com.ShopSphere.e_commerce.Exception.ProductNotFoundException;
import com.ShopSphere.e_commerce.Exception.UserNotFoundException;
import com.ShopSphere.e_commerce.Repository.OrderRepository;
import com.ShopSphere.e_commerce.Repository.ProductRepository;
import com.ShopSphere.e_commerce.Repository.ReviewRepository;
import com.ShopSphere.e_commerce.Repository.UserRepository;
import com.ShopSphere.e_commerce.Service.ReviewService;
import com.ShopSphere.e_commerce.dto.ReviewRequestDto;
import com.ShopSphere.e_commerce.dto.ReviewResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,  ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ReviewResponseDto addReview(Long productId, ReviewRequestDto reviewRequestDto){
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product With id " +  productId + " not found"));

        List<Order> orders = orderRepository.findByUser(user);

        boolean hasPurchased = false;
        for(Order order : orders){
            if(order.getOrderStatus() == OrderStatus.DELIVERED){
                for (OrderItem orderItem : order.getOrderItems()) {
                    if(orderItem.getProduct().getId().equals(product.getId())){
                        hasPurchased = true;
                        break;
                    }
                }
            }
        }

        if(!hasPurchased){
            throw  new IllegalStateException("You can review only purchased and delivered products");
        }

        if(reviewRepository.findByUserIdAndProductId(user.getId(), productId).isPresent()){
            throw new IllegalStateException(("You have already reviewed this product"));
        }

        Review review = new Review();
        review.setRating(reviewRequestDto.getRating());
        review.setComment(reviewRequestDto.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);

        return new ReviewResponseDto(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getComment(),
                user.getName(),
                savedReview.getProduct().getId(),
                savedReview.getCreatedAt()
        );
    }

    @Override
    public List<ReviewResponseDto> getReviewsByProduct(Long productId){
       productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product With id " +  productId + " not found"));

        List<Review> reviews = reviewRepository.findByProductId(productId);

        return reviews.stream().map(review -> {
            return new ReviewResponseDto(
                    review.getId(),
                    review.getRating(),
                    review.getComment(),
                    review.getUser().getName(),
                    review.getProduct().getId(),
                    review.getCreatedAt()
            );
        }).toList();
    }

}
