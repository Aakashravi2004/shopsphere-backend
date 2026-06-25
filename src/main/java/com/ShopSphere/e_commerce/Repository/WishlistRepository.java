package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.Product;
import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.Entity.Wishlist;
import com.ShopSphere.e_commerce.dto.WishlistResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository  extends JpaRepository<Wishlist,Long> {

    List<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUserAndProduct(User user, Product product);

}
