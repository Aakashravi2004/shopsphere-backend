package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.*;
import com.ShopSphere.e_commerce.Enum.OrderStatus;
import com.ShopSphere.e_commerce.Exception.CartEmptyException;
import com.ShopSphere.e_commerce.Exception.CartNotFoundException;
import com.ShopSphere.e_commerce.Exception.OrderNotFoundException;
import com.ShopSphere.e_commerce.Exception.UserNotFoundException;
import com.ShopSphere.e_commerce.Repository.CartItemRepository;
import com.ShopSphere.e_commerce.Repository.CartRepository;
import com.ShopSphere.e_commerce.Repository.OrderRepository;
import com.ShopSphere.e_commerce.Repository.UserRepository;
import com.ShopSphere.e_commerce.Service.OrderService;
import com.ShopSphere.e_commerce.dto.OrderItemResponseDto;
import com.ShopSphere.e_commerce.dto.OrderResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,  CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public OrderResponseDto placeOrder() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart with id " + user.getId() + " not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if(cartItems.isEmpty()){
            throw new CartEmptyException("Cannot place order because cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (CartItem item : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            totalPrice += item.getProduct().getPrice()*item.getQuantity();
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        List<OrderItemResponseDto> itemDtos = savedOrder.getOrderItems().stream()
                .map(orderItem -> {
                    Double subtotal = orderItem.getPrice()*orderItem.getQuantity();
                    return  new  OrderItemResponseDto(
                            orderItem.getProduct().getId(),
                            orderItem.getProduct().getName(),
                            orderItem.getPrice(),
                            orderItem.getQuantity(),
                            subtotal
                    );
                }).toList();

        return new OrderResponseDto(
                savedOrder.getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderDate(),
                itemDtos
        );
    }

    @Override
    public List<OrderResponseDto> getMyOrders(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(order -> {
                    List<OrderItemResponseDto> itemDtos = order.getOrderItems()
                            .stream().map(orderItem -> {
                                double subtotal = orderItem.getPrice()*orderItem.getQuantity();
                                return new   OrderItemResponseDto(
                                        orderItem.getProduct().getId(),
                                        orderItem.getProduct().getName(),
                                        orderItem.getPrice(),
                                        orderItem.getQuantity(),
                                        subtotal
                                );
                            }).toList();


                    return new OrderResponseDto(
                            order.getId(),
                            order.getTotalPrice(),
                            order.getOrderStatus(),
                            order.getOrderDate(),
                            itemDtos
                    );

                }).toList();

    }

    @Override
    public OrderResponseDto payOrder(Long orderId){

        // step 1 : get LoggedIn User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        //step 2 : find order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));

        // step 3 : Verify Owner
        // User A should not pay User B's order.
        if(!order.getUser().getId().equals(user.getId())){
            throw new OrderNotFoundException("Order does not belong to this user");
        }

        // step 4 : Already Paid Check
        if(order.getOrderStatus() ==  OrderStatus.PAID){
            throw  new IllegalStateException("Order with id " + orderId + " is already paid");
        }

        // step 5 : Update Status
        order.setOrderStatus(OrderStatus.PAID);

        // step 6 : save
        Order savedOrder = orderRepository.save(order);

        // step 7 : fetch the cart to delete because after payment become paid we need to remove cart items
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart with id " + user.getId() + " not found"));

        // step 8 : fetch cartItems
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        // step 9 : delete the cartItems
        cartItemRepository.deleteAll(cartItems);

        List<OrderItemResponseDto> itemDtos = savedOrder.getOrderItems().stream()
                .map(orderItem -> {
                    Double subtotal = orderItem.getPrice()*orderItem.getQuantity();
                    return  new  OrderItemResponseDto(
                            orderItem.getProduct().getId(),
                            orderItem.getProduct().getName(),
                            orderItem.getPrice(),
                            orderItem.getQuantity(),
                            subtotal
                    );
                }).toList();

        return new OrderResponseDto(
                savedOrder.getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderDate(),
                itemDtos
        );

    }

}
