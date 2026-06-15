package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Service.CartService;
import com.ShopSphere.e_commerce.dto.AddToCartRequestDto;
import com.ShopSphere.e_commerce.dto.AddToCartResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<AddToCartResponseDto> addToCart(@Valid @RequestBody AddToCartRequestDto addToCartRequestDto) {
        return ResponseEntity.ok(cartService.addToCart(addToCartRequestDto));
    }

}
