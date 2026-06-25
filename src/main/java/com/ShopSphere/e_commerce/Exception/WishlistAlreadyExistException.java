package com.ShopSphere.e_commerce.Exception;

public class WishlistAlreadyExistException extends RuntimeException {
    public WishlistAlreadyExistException(String message) {
        super(message);
    }
}
