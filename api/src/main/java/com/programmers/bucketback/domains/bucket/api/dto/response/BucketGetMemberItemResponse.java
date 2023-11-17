package com.programmers.bucketback.domains.bucket.api.dto.response;

import java.util.List;

import com.programmers.bucketback.common.cursor.CursorSummary;
import com.programmers.bucketback.domains.bucket.model.BucketMemberItemSummary;

public record BucketGetMemberItemResponse(
	String nextCursorId,
	int summaryCount,
	List<BucketMemberItemSummary> summaries
) {
	public static BucketGetMemberItemResponse from(final CursorSummary<BucketMemberItemSummary> summary) {
		return new BucketGetMemberItemResponse(
			summary.nextCursorId(),
			summary.summaryCount(),
			summary.summaries()
		);
	}
}
