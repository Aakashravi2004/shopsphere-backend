package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.*;

import java.util.List;

public interface CartService {

    AddToCartResponseDto addToCart(AddToCartRequestDto dto);
    CartResponseDto getCart();
    RemoveFromCartResponseDto removeFromCart(Long productId);
    UpdateCartItemResponseDto updateCartItem(Long productId, UpdateCartItemRequestDto dto);

}
