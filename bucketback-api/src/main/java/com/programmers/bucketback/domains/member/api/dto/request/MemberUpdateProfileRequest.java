package com.programmers.bucketback.domains.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberUpdateProfileRequest(
		@Schema(description = "닉네임", example = "best_kim")
		@Size(min = 3, max = 25, message = "닉네임은 3글자에서 25글자 사이여야 합니다.")
		@Pattern(regexp = "^[A-Za-z0-9_]+$", message = "닉네임은 영어 대소문자, 숫자 그리고 언더스코어만 허용합니다.")
		@NotNull(message = "닉네임은 필수 값입니다.")
		String nickname,

		@Schema(description = "자기소개", example = "안녕! 나는 베스트김이야.")
		@Size(max = 300, message = "자기소개는 최대 300자 입니다.")
		@NotNull(message = "자기소개는 필수 값입니다.")
		String introduction
) {
}
