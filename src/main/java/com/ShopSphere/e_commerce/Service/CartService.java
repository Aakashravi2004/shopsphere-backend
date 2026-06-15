package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.AddToCartRequestDto;
import com.ShopSphere.e_commerce.dto.AddToCartResponseDto;
import com.ShopSphere.e_commerce.dto.CartResponseDto;

import java.util.List;

public interface CartService {

    AddToCartResponseDto addToCart(AddToCartRequestDto dto);
    CartResponseDto getCart();
}
