package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Enum.OrderStatus;
import com.ShopSphere.e_commerce.Service.OrderService;
import com.ShopSphere.e_commerce.dto.OrderRequestDto;
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
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.placeOrder(orderRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(){
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponseDto> payOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.payOrder(orderId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus orderStatus){

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatus));

    }

}
