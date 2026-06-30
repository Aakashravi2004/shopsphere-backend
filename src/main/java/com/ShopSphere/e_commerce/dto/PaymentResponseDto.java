package com.ShopSphere.e_commerce.dto;

import com.ShopSphere.e_commerce.Enum.PaymentMethod;
import com.ShopSphere.e_commerce.Enum.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    private Long paymentId;

    private Long orderId;

    private Double amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String transactionId;

    private LocalDateTime paymentDate;

}
