package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.Order;
import com.ShopSphere.e_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}
