package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.Cart;
import com.ShopSphere.e_commerce.Entity.CartItem;
import com.ShopSphere.e_commerce.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart card, Product product);

}
