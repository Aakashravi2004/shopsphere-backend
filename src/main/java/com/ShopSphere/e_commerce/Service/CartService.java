package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.AddToCartRequestDto;
import com.ShopSphere.e_commerce.dto.AddToCartResponseDto;

public interface CartService {

    AddToCartResponseDto addToCart(AddToCartRequestDto dto);

}
