package com.programmers.bucketback.domains.bucket.application;

import java.util.List;

import com.programmers.bucketback.domains.bucket.domain.Bucket;
import com.programmers.bucketback.domains.common.Hobby;
import lombok.Builder;

@Builder
public record BucketContent(

	Hobby hobby,
	String name,
	Integer budget,
	List<Long> itemIds,
	Long memberId,
	Long bucketId
) {
	public static BucketContent from(final Bucket bucket){
		return BucketContent.builder()
			.hobby(bucket.getHobby())
			.name(bucket.getBucketInfo().getName())
			.budget(bucket.getBucketInfo().getBudget())
			.bucketId(bucket.getId())
			.memberId(bucket.getMemberId())
			.build();
	}
}
