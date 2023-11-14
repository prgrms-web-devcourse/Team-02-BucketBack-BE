package com.programmers.bucketback.domains.bucket.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.programmers.bucketback.domains.bucket.application.vo.BucketCursorSummary;
import com.programmers.bucketback.domains.bucket.application.vo.BucketGetServiceResponse;
import com.programmers.bucketback.domains.bucket.application.vo.BucketMemberItemCursorSummary;
import com.programmers.bucketback.domains.bucket.application.vo.BucketMemberItemSummary;
import com.programmers.bucketback.domains.bucket.application.vo.BucketSummary;
import com.programmers.bucketback.domains.bucket.domain.Bucket;
import com.programmers.bucketback.domains.bucket.domain.BucketItem;
import com.programmers.bucketback.domains.bucket.repository.BucketItemRepository;
import com.programmers.bucketback.domains.bucket.repository.BucketRepository;
import com.programmers.bucketback.domains.common.Hobby;
import com.programmers.bucketback.domains.common.vo.CursorPageParameters;
import com.programmers.bucketback.domains.item.application.ItemReader;
import com.programmers.bucketback.domains.item.application.MemberItemReader;
import com.programmers.bucketback.domains.item.application.vo.ItemInfo;
import com.programmers.bucketback.global.error.exception.EntityNotFoundException;
import com.programmers.bucketback.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BucketReader {

	private final BucketRepository bucketRepository;
	private final BucketItemRepository bucketItemRepository;
	private final MemberItemReader memberItemReader;
	private final ItemReader itemReader;

	/** 버킷 정보 조회 */
	public Bucket read(final Long bucketId) {
		return bucketRepository.findById(bucketId)
			.orElseThrow(() -> {
				throw new EntityNotFoundException(ErrorCode.BUCKET_NOT_FOUND);
			});
	}

	public Bucket read(
		final Long bucketId,
		final Long memberId
	) {
		return bucketRepository.findByIdAndMemberId(bucketId, memberId)
			.orElseThrow(() -> {
				throw new EntityNotFoundException(ErrorCode.BUCKET_NOT_FOUND);
			});
	}

	/** 버킷 아이템 정보 조회 */
	public List<BucketItem> bucketItemRead(final Long bucketId) {
		return bucketItemRepository.findByBucketId(bucketId);
	}

	/** 버킷 수정을 위한 MemberItem 커서 조회 */
	public BucketMemberItemCursorSummary readByMemberItems(
		final Long bucketId,
		final Long memberId,
		final CursorPageParameters parameters
	) {
		int pageSize = parameters.size() == 0 ? 20 : parameters.size();

		Bucket bucket = read(bucketId, memberId);
		List<Long> itemIdsFromBucketItem = bucket.getBucketItems().stream()
			.map(bucketItem -> bucketItem.getItem().getId())
			.toList();
		List<Long> itemIdsFromMemberItem = memberItemReader.readByMemberId(memberId).stream()
			.map(memberItem -> memberItem.getItem().getId())
			.toList();

		List<BucketMemberItemSummary> summaries = memberItemReader.readBucketMemberItem(
			itemIdsFromBucketItem,
			itemIdsFromMemberItem,
			memberId,
			parameters.cursorId(),
			pageSize
		);

		String nextCursorId = summaries.size() == 0 ? null : summaries.get(summaries.size() - 1).cursorId();
		int summaryCount = summaries.size();

		return new BucketMemberItemCursorSummary(nextCursorId, summaryCount, summaries);
	}

	/** 버킷 정보 커서 페이징 조회 */
	public BucketCursorSummary readByCursor(
		final Long memberId,
		final Hobby hobby,
		final CursorPageParameters parameters
	) {
		int pageSize = parameters.size() == 0 ? 20 : parameters.size();

		List<BucketSummary> summaries = bucketRepository.findAllByCursor(
			memberId,
			hobby,
			parameters.cursorId(),
			pageSize
		);

		String nextCursorId = summaries.size() == 0 ? null : summaries.get(summaries.size() - 1).cursorId();
		int summaryCount = summaries.size();

		return new BucketCursorSummary(nextCursorId, summaryCount, summaries);
	}

	/**
	 * 버킷 정보 상세 조회
	 */
	public BucketGetServiceResponse readDetail(final Long bucketId) {
		Bucket bucket = read(bucketId);
		List<ItemInfo> itemInfos = bucket.getBucketItems().stream()
			.map(bucketItem -> itemReader.read(bucketItem.getItem().getId()))
			.map(item -> ItemInfo.from(item))
			.toList();

		return new BucketGetServiceResponse(bucket, itemInfos);
	}
}
