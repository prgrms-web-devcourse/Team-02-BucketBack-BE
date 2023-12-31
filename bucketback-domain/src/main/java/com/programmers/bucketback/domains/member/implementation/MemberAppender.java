package com.programmers.bucketback.domains.member.implementation;

import org.springframework.stereotype.Component;

import com.programmers.bucketback.domains.member.domain.Member;
import com.programmers.bucketback.domains.member.domain.vo.Role;
import com.programmers.bucketback.domains.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberAppender {

	private final MemberRepository memberRepository;

	public void append(
		final Member member
	) {
  		memberRepository.save(member);
	}

	public void append(
		final String email,
		final String encodedPassword,
		final String nickname
	) {
		final Member member = Member.builder()
			.email(email)
			.password(encodedPassword)
			.nickname(nickname)
			.role(Role.USER)
			.build();

		memberRepository.save(member);
	}
}
