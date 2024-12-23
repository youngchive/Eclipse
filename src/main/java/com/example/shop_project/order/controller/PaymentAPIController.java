package com.example.shop_project.order.controller;
import com.example.shop_project.order.dto.OrderDetailDto;
import com.example.shop_project.order.dto.PaymentDto;
import com.example.shop_project.order.entity.Payment;
import com.example.shop_project.order.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentAPIController {
    @Autowired
    PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@Validated @RequestBody PaymentDto paymentDto){
        Payment payment = paymentService.createPayment(paymentDto);
        return ResponseEntity.created(URI.create("/")).body(payment);
    }

    @PatchMapping("/decrease-product-stock")
    public ResponseEntity<Void> decreaseProductStock(@Validated @RequestBody List<OrderDetailDto> orderDetailDtoList){
        paymentService.decreaseProductStock(orderDetailDtoList);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/restore-product-stock")
    public ResponseEntity<Void> restoreProductStock(@Validated @RequestBody List<OrderDetailDto> orderDetailDtoList){
        paymentService.productStockRollback(orderDetailDtoList);
        return ResponseEntity.ok().build();
    }
}
