package com.ShopSphere.e_commerce.dto;

import com.ShopSphere.e_commerce.Enum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private List<OrderItemResponseDto> items;

}
