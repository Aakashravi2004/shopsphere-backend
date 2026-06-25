package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Service.WishlistService;
import com.ShopSphere.e_commerce.dto.WishlistResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService  wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishlistResponseDto> addToWishlist(@PathVariable Long productId){
        return ResponseEntity.ok(wishlistService.addToWishlist(productId));
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(){
        return ResponseEntity.ok(wishlistService.getWishlist());
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable Long wishlistId){
        wishlistService.removeFromWishlist(wishlistId);
        return ResponseEntity.ok("Wishlist item removed successfully");
    }
}
