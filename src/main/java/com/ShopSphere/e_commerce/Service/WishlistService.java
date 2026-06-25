package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.WishlistResponseDto;

import java.util.List;

public interface WishlistService {

    WishlistResponseDto addToWishlist(Long productId);
    List<WishlistResponseDto> getWishlist();
    void removeFromWishlist(Long wishlistId);

}
