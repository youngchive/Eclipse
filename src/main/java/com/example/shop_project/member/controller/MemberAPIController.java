package com.example.shop_project.member.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project.jwt.dto.JwtTokenDto;
import com.example.shop_project.jwt.dto.JwtTokenResponse;
import com.example.shop_project.member.repository.MemberRepository;
import com.example.shop_project.member.service.MemberService;
import com.example.shop_project.oauth2.GoogleUserInfoDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberAPIController {
	private final MemberRepository memberRepository;
	private final MemberService memberService;
	
	@GetMapping("/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
		boolean isExists = memberRepository.existsByEmail(email);
        Map<String, Boolean> response = Collections.singletonMap("exists", isExists);

        // 이메일 중복 시 409 Conflict 반환, 그렇지 않으면 200 OK
        if (isExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
	}
	
	@GetMapping("/check-nickname")
	public ResponseEntity<Map<String, Boolean>> checkNickname(
            @RequestParam("nickname") String nickname,
            @RequestParam(value = "currentNickname", required = false) String currentNickname) {
        
        // 현재 닉네임과 동일하면 중복으로 간주하지 않음
        boolean isExists = (currentNickname == null || !nickname.equals(currentNickname)) 
                			&& memberRepository.existsByNickname(nickname);
        Map<String, Boolean> response = Collections.singletonMap("exists", isExists);

        // 닉네임 중복 시 409 Conflict 반환, 그렇지 않으면 200 OK
        if (isExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }
	
	
	 @GetMapping("/userinfo")
	    public ResponseEntity<JwtTokenResponse> getUserInfo(
	            @AuthenticationPrincipal OAuth2User principal,
	            HttpServletResponse response
	    ) {
	        if (principal != null) {
	            JwtTokenDto jwtTokenDto = memberService.googleLogin(GoogleUserInfoDto.builder()
	                    .email((String) principal.getAttribute("email"))
	                    .name((String) principal.getAttribute("name"))
	                    .providerId((String) principal.getAttribute("sub"))
	                    .build()
	            );

	            // Refresh Token 쿠키 설정
	            Cookie refreshTokenCookie = new Cookie(
	                    "refreshToken",
	                    jwtTokenDto.getRefreshToken()
	            );

	            refreshTokenCookie.setHttpOnly(true); // JavaScript 접근 차단
	            refreshTokenCookie.setSecure(true);  // HTTPS에서만 전송
	            refreshTokenCookie.setPath("/");     // 모든 경로에서 접근 가능
	            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효

	            response.addCookie(refreshTokenCookie);

	            return ResponseEntity.ok().body(JwtTokenResponse.builder()
	                    .accessToken(jwtTokenDto.getAccessToken())
	                    .build());
	        } else {
	            return ResponseEntity.ok().body(JwtTokenResponse.builder()
	                    .accessToken("null")
	                    .build());
	        }
	    }
}
