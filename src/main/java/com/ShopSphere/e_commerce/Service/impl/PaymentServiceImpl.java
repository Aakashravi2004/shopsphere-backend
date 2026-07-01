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

    @Override
    @Transactional
    public PaymentResponseDto refundOrder(Long orderId){

        // step 1 : Authentication
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        //step 2 : find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  UserNotFoundException("User with email " + email + " not found"));

        //step 3 : find the order based on user and orderId
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new  OrderNotFoundException("Order With Id " + orderId + " Not Found"));

        // step 4 : check weather the order is paid or not
        if(!OrderStatus.PAID.equals(order.getOrderStatus())){
            throw new IllegalStateException("Only paid orders can be refunded");
        }

        // step 5 : if order paid find payment
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new  PaymentNotFoundException("Payment Not Found"));

        // step 6 : check payment status success
        if(!PaymentStatus.SUCCESS.equals(payment.getPaymentStatus())){
            throw new IllegalStateException("Only successful payments can be refunded");
        }

        // step 7 : restore the stock
        for(OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int incrementingStock = product.getStockQuantity() + item.getQuantity();
            product.setStockQuantity(incrementingStock);
            productRepository.save(product);
        }

        //step 7 : modify new entity status in paymentStatus
        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        //step 8 : modify new entity status in orderStatus
        order.setOrderStatus(OrderStatus.REFUNDED);

        // step 9 : save the payment to modify the changes in db
        Payment savedPayment = paymentRepository.save(payment);

        // step 10: save the order
        orderRepository.save(order);

        //Step 11 : return response
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



}
