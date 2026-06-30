package com.ShopSphere.e_commerce.Repository;

import com.ShopSphere.e_commerce.Entity.Order;
import com.ShopSphere.e_commerce.Entity.Payment;
import com.ShopSphere.e_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUser(User user);

    Optional<Payment> findByOrder(Order order);

}
