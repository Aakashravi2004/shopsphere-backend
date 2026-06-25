package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.Product;
import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.Entity.Wishlist;
import com.ShopSphere.e_commerce.Exception.ProductNotFoundException;
import com.ShopSphere.e_commerce.Exception.UserNotFoundException;
import com.ShopSphere.e_commerce.Exception.WishlistAlreadyExistException;
import com.ShopSphere.e_commerce.Exception.WishlistNotFoundException;
import com.ShopSphere.e_commerce.Repository.ProductRepository;
import com.ShopSphere.e_commerce.Repository.UserRepository;
import com.ShopSphere.e_commerce.Repository.WishlistRepository;
import com.ShopSphere.e_commerce.Service.RatingService;
import com.ShopSphere.e_commerce.Service.WishlistService;
import com.ShopSphere.e_commerce.dto.WishlistResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RatingService ratingService;

    public WishlistServiceImpl(WishlistRepository wishlistRepository,  UserRepository userRepository, ProductRepository productRepository, RatingService ratingService) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.ratingService = ratingService;
    }

    @Override
    public WishlistResponseDto addToWishlist(Long productId){
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        if(wishlistRepository.findByUserAndProduct(user,product).isPresent()){
            throw new WishlistAlreadyExistException("Wishlist already exists");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setCreatedAt(LocalDateTime.now());

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return new WishlistResponseDto(
                savedWishlist.getId(),
                savedWishlist.getProduct().getId(),
                savedWishlist.getProduct().getName(),
                savedWishlist.getProduct().getPrice(),
                savedWishlist.getProduct().getImageUrl(),
                ratingService.calculateAverageRating(product.getId()),
                savedWishlist.getCreatedAt()
        );
    }

    @Override
    public List<WishlistResponseDto> getWishlist(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        List<Wishlist> wishlists = wishlistRepository.findByUser(user);

        return wishlists.stream().map(wishlist -> {
            return new WishlistResponseDto(
                    wishlist.getId(),
                    wishlist.getProduct().getId(),
                    wishlist.getProduct().getName(),
                    wishlist.getProduct().getPrice(),
                    wishlist.getProduct().getImageUrl(),
                    ratingService.calculateAverageRating(wishlist.getProduct().getId()),
                    wishlist.getCreatedAt()
            );
        }).toList();

    }

    @Override
    public void removeFromWishlist(Long wishlistId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist with id " + wishlistId + " not found"));

        if(!user.getId().equals(wishlist.getUser().getId())){
            throw new IllegalStateException("You are not authorized to remove this wishlist item");
        }

        wishlistRepository.delete(wishlist);

    }

}
