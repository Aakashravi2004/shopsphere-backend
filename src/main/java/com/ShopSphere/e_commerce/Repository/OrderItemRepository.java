package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
