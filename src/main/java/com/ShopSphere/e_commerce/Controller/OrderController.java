package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Service.OrderService;
import com.ShopSphere.e_commerce.dto.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDto> placeOrder(){
        return ResponseEntity.ok(orderService.placeOrder());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(){
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponseDto> payOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.payOrder(orderId));
    }

}
