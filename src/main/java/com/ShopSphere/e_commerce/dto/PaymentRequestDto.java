package com.ShopSphere.e_commerce.dto;

import com.ShopSphere.e_commerce.Enum.PaymentMethod;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {

    private PaymentMethod paymentMethod;

}
