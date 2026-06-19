package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder();
    List<OrderResponseDto> getMyOrders();
    OrderResponseDto payOrder(Long orderId);

}
