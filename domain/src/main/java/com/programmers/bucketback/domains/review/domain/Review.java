package com.programmers.bucketback.domains.review.domain;

import java.util.Objects;

import com.programmers.bucketback.domains.BaseEntity;
import com.programmers.bucketback.domains.review.model.ReviewContent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull
	@JoinColumn(name = "items_id", nullable = false)
	private Long itemId;

	@NotNull
	@Column(name = "members_id", nullable = false)
	private Long memberId;

	@Column(name = "content", nullable = false)
	private String content;

	@NotNull
	@Column(name = "rating", nullable = false)
	private Integer rating;

	@Builder
	public Review(
		@NotNull final Long itemId,
		@NotNull final Long memberId,
		@NotNull final String content,
		@NotNull final Integer rating
	) {
		this.itemId = Objects.requireNonNull(itemId);
		this.memberId = Objects.requireNonNull(memberId);
		this.content = Objects.requireNonNull(content);
		this.rating = Objects.requireNonNull(rating);
	}

	public void changeReviewContent(final ReviewContent reviewContent) {
		this.content = reviewContent.content();
		this.rating = reviewContent.rating();
	}

	public boolean containsItem(final Long itemId) {
		return this.itemId.equals(itemId);
	}

	public boolean isOwner(final Long memberId) {
		return this.memberId.equals(memberId);
	}
}
