package com.example.shop_project.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends OrderBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNo;
    @Column(nullable = false)
    private Long totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayMethod payMethod;
    // 직접 입력을 위해 String
    @Column(nullable = false)
    private String requirement;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String addressDetail;
    @Column(nullable = false, length = 5)
    private String postNo;
    // member

    @Builder
    public Order(Long totalPrice, OrderStatus orderStatus, PayMethod payMethod, String requirement, String address, String addressDetail, String postNo){
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.payMethod = payMethod;
        this.requirement = requirement;
        this.address = address;
        this.addressDetail = addressDetail;
        this.postNo = postNo;
    }

}
