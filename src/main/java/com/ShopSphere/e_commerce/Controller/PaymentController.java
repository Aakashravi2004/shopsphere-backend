package com.ShopSphere.e_commerce.Controller;

import com.ShopSphere.e_commerce.Service.PaymentService;
import com.ShopSphere.e_commerce.dto.PaymentRequestDto;
import com.ShopSphere.e_commerce.dto.PaymentResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponseDto> payOrder(@PathVariable Long orderId,@Valid @RequestBody PaymentRequestDto paymentRequestDto){
        return ResponseEntity.ok(paymentService.payOrder(orderId, paymentRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getMyPayments(){
        return ResponseEntity.ok(paymentService.getMyPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable Long paymentId){
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @PatchMapping("/{orderId}/refund")
    public ResponseEntity<PaymentResponseDto> refundOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.refundOrder(orderId));
    }

}

