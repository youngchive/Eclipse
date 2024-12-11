package com.example.shop_project;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.shop_project.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	private final MemberRepository memberRepository;
	
	public void Join(Member member) {
		// 이메일과 닉네임 중복 검사 (비동기로 중복 검사 하지만 안정성을 위해 추가)
	    Optional<Member> existingMemberByEmail = memberRepository.findByEmail(member.getEmail());
	    if (existingMemberByEmail.isPresent()) {
	        throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
	    }
	    Optional<Member> existingMemberByNickname = memberRepository.findByNickname(member.getNickname());
	    if (existingMemberByNickname.isPresent()) {
	        throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
	    }
		
		memberRepository.save(member);	
	}
}
