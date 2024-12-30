package com.example.shop_project.inquiry.controller;

import com.example.shop_project.inquiry.entity.InquiryRequestDto;
import com.example.shop_project.inquiry.entity.Inquiry;
import com.example.shop_project.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/products/{productId}/inquiries")
@RequiredArgsConstructor
public class InquiryViewController {

    private final InquiryService inquiryService;

    // 특정 상품의 문의 목록 페이지 렌더링
    @GetMapping
    public String showInquiriesByProduct(@PathVariable Long productId, Model model) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Inquiry> inquiries = inquiryService.getInquiriesByProductId(productId, currentUserEmail);

        model.addAttribute("inquiries", inquiries);
        model.addAttribute("productId", productId);

        return "inquiry/list";
    }

    // 특정 상품의 문의 작성 페이지
    @GetMapping("/create")
    public String showCreatePage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);

        return "inquiry/create";
    }

    // 특정 상품의 문의 생성 처리
    @PostMapping
    public String createInquiry(
            @PathVariable Long productId,
            @Valid @ModelAttribute InquiryRequestDto dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        inquiryService.createInquiry(productId, dto, userEmail);

        return "redirect:/products/" + productId + "/inquiries";
    }

    // 특정 상품의 특정 문의 조회
    @GetMapping("/{inquiryId}")
    public String getInquiryById(@PathVariable Long productId, @PathVariable Long inquiryId, Model model) {
        Inquiry inquiry = inquiryService.getInquiryByProductIdAndInquiryId(productId, inquiryId);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        String nickname = inquiry.getMember().getNickname();

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("productId", productId);
        model.addAttribute("nickname", nickname);
        model.addAttribute("userEmail", userEmail);

        return "inquiry/detail";
    }

}