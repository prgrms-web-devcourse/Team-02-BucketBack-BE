package com.programmers.bucketback.domains.review.application;

import com.programmers.bucketback.domains.common.MemberUtils;
import com.programmers.bucketback.domains.common.vo.CursorPageParameters;
import com.programmers.bucketback.domains.review.application.dto.GetReviewByCursorServiceResponse;
import com.programmers.bucketback.domains.review.application.dto.ReviewContent;
import com.programmers.bucketback.global.level.PayPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewAppender reviewAppender;
	private final ReviewModifier reviewModifier;
	private final ReviewValidator reviewValidator;
	private final ReviewCursorReader reviewCursorReader;
	private final ReviewRemover reviewRemover;

	@PayPoint(15)
	public Long createReview(
		final Long itemId,
		final ReviewContent reviewContent
	) {
		Long memberId = MemberUtils.getCurrentMemberId();
		reviewAppender.append(itemId, memberId, reviewContent);

		return memberId;
	}

	public void updateReview(
		final Long itemId,
		final Long reviewId,
		final ReviewContent reviewContent
	) {
		Long memberId = MemberUtils.getCurrentMemberId();
		reviewValidator.validItemReview(itemId, reviewId);
		reviewValidator.validOwner(reviewId, memberId);
		reviewModifier.modify(reviewId, reviewContent);
	}

	public GetReviewByCursorServiceResponse getReviewsByCursor(
		final Long itemId,
		final CursorPageParameters parameters
	) {
		return reviewCursorReader.readByCursor(itemId, parameters);
	}

	public void deleteReview(
		final Long itemId,
		final Long reviewId
	) {
		Long memberId = MemberUtils.getCurrentMemberId();
		reviewValidator.validItemReview(itemId, reviewId);
		reviewValidator.validOwner(reviewId, memberId);
		reviewRemover.remove(reviewId);
	}
}
