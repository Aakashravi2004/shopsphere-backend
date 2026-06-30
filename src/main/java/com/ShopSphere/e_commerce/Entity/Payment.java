    package com.ShopSphere.e_commerce.Entity;

    import com.ShopSphere.e_commerce.Enum.PaymentMethod;
    import com.ShopSphere.e_commerce.Enum.PaymentStatus;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "payments")
    public class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Double amount;

        @Enumerated(EnumType.STRING)
        private PaymentStatus paymentStatus;

        @Enumerated(EnumType.STRING)
        private PaymentMethod paymentMethod;

        private String transactionId;
        private LocalDateTime  paymentDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id")
        private Order  order;

    }
