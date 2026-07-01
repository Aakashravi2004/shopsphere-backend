package com.ShopSphere.e_commerce.Service;

import com.ShopSphere.e_commerce.dto.PaymentRequestDto;
import com.ShopSphere.e_commerce.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto payOrder(Long orderId, PaymentRequestDto paymentRequestDto);

    List<PaymentResponseDto> getMyPayments();

    PaymentResponseDto getPaymentById(Long paymentId);

    PaymentResponseDto refundOrder(Long orderId);

}
