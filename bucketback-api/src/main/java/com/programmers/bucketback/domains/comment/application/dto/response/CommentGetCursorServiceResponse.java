package com.programmers.bucketback.domains.comment.application.dto.response;

import com.programmers.bucketback.common.cursor.CursorSummary;
import com.programmers.bucketback.domains.comment.repository.CommentSummary;

public record CommentGetCursorServiceResponse(
	CursorSummary<CommentSummary> commentSummary,
	int totalCommentCount
) {
}
