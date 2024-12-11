package com.example.shop_project.member.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shop_project.member.dto.MemberRequestDTO;
import com.example.shop_project.member.entity.Member;
import com.example.shop_project.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public void Join(MemberRequestDTO memberDTO) {
		// 이메일과 닉네임 중복 검사 (비동기로 중복 검사 하지만 안정성을 위해 추가)
	    Optional<Member> existingMemberByEmail = memberRepository.findByEmail(memberDTO.getEmail());
	    if (existingMemberByEmail.isPresent()) {
	        throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
	    }
	    Optional<Member> existingMemberByNickname = memberRepository.findByNickname(memberDTO.getNickname());
	    if (existingMemberByNickname.isPresent()) {
	        throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
	    }
	    
	    // 비밀번호 암호화
	    String encryptedPassword = passwordEncoder.encode(memberDTO.getPassword());
	    
	    Member member = new Member();
        member.setEmail(memberDTO.getEmail());
        member.setNickname(memberDTO.getNickname());
        member.setPassword(encryptedPassword);
        member.setPhone(memberDTO.getPhone());
        
        memberRepository.save(member);	
	}
}
