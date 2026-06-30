package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.*;
import com.ShopSphere.e_commerce.Enum.OrderStatus;
import com.ShopSphere.e_commerce.Enum.PaymentStatus;
import com.ShopSphere.e_commerce.Exception.CartNotFoundException;
import com.ShopSphere.e_commerce.Exception.OrderNotFoundException;
import com.ShopSphere.e_commerce.Exception.PaymentNotFoundException;
import com.ShopSphere.e_commerce.Exception.UserNotFoundException;
import com.ShopSphere.e_commerce.Repository.*;
import com.ShopSphere.e_commerce.Service.PaymentService;
import com.ShopSphere.e_commerce.dto.PaymentRequestDto;
import com.ShopSphere.e_commerce.dto.PaymentResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository, OrderRepository orderRepository,  ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis();
    }

    @Override
    @Transactional
    public PaymentResponseDto payOrder(Long orderId, PaymentRequestDto paymentRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User With Email " + email + " Not Found"));

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new OrderNotFoundException("Order With orderId " +  orderId + " Not Found"));

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new PaymentNotFoundException("Payment Not Found"));

        if(PaymentStatus.SUCCESS.equals(payment.getPaymentStatus())) throw new IllegalStateException("Order is already paid");

        if(OrderStatus.CANCELLED.equals(order.getOrderStatus())) throw new IllegalStateException("Cancelled orders cannot be paid");

        // Stock Management
        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            if (item.getQuantity() > product.getStockQuantity()) {
                throw new IllegalStateException(
                        product.getName() + " has only "
                                + product.getStockQuantity()
                                + " items available");
            }

            int remainingStock =
                    product.getStockQuantity() - item.getQuantity();

            product.setStockQuantity(remainingStock);

            productRepository.save(product);
        }

        payment.setPaymentMethod(paymentRequestDto.getPaymentMethod());

        payment.setTransactionId(generateTransactionId());

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        payment.setPaymentDate(LocalDateTime.now());

        order.setOrderStatus(OrderStatus.PAID);

        Payment savedPayment = paymentRepository.save(payment);

        Order savedOrder = orderRepository.save(order);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        cartItemRepository.deleteAll(cartItems);

        return new PaymentResponseDto(
                savedPayment.getId(),
                savedPayment.getOrder().getId(),
                savedPayment.getAmount(),
                savedPayment.getPaymentMethod(),
                savedPayment.getPaymentStatus(),
                savedPayment.getTransactionId(),
                savedPayment.getPaymentDate()
        );
    }

    @Override
    public List<PaymentResponseDto> getMyPayments(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with email " + email + " not found"));

        List<Payment> payments =
                paymentRepository.findByUser(user);

        return payments.stream().map(payment -> new PaymentResponseDto(
                    payment.getId(),
                    payment.getOrder().getId(),
                    payment.getAmount(),
                    payment.getPaymentMethod(),
                    payment.getPaymentStatus(),
                    payment.getTransactionId(),
                    payment.getPaymentDate()
            )).toList();
    }

    @Override
    public PaymentResponseDto getPaymentById(Long paymentId){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  UserNotFoundException("User with email " + email + " not found"));

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new  PaymentNotFoundException("Payment With Id " + paymentId  +  " Not Found"));

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new PaymentNotFoundException("Payment Not Found");
        }

        return new PaymentResponseDto(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTransactionId(),
                payment.getPaymentDate()
        );


    }



}
