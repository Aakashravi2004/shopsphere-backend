package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.Enum.OrderStatus;
import com.ShopSphere.e_commerce.dto.OrderRequestDto;
import com.ShopSphere.e_commerce.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto);
    List<OrderResponseDto> getMyOrders();
    OrderResponseDto payOrder(Long orderId);
    OrderResponseDto getOrderById(Long orderId);
    OrderResponseDto cancelOrder(Long orderId);

    //Admin or seller
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus orderStatus);

}
