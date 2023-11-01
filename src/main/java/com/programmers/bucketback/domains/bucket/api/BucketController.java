package com.programmers.bucketback.domains.bucket.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.programmers.bucketback.domains.bucket.api.dto.request.BucketCreateRequest;
import com.programmers.bucketback.domains.bucket.api.dto.request.BucketGetByCursorRequest;
import com.programmers.bucketback.domains.bucket.api.dto.request.BucketUpdateRequest;
import com.programmers.bucketback.domains.bucket.api.dto.response.GetBucketResponse;
import com.programmers.bucketback.domains.bucket.api.dto.response.GetBucketsByCursorResponse;
import com.programmers.bucketback.domains.bucket.application.BucketService;
import com.programmers.bucketback.domains.common.Hobby;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "buckets", description = "버킷 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BucketController {

	private final BucketService bucketService;

	@Operation(summary = "버킷 생성", description = "FundingCreateRequestDTO 을 이용하여 버킷을 생성힙니다.")
	@PostMapping("/buckets")
	public ResponseEntity<Void> createBucket(@RequestBody @Valid final BucketCreateRequest request){
		bucketService.createBucket(request.toContent()); //memberId값 필요함

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "버킷 수정", description = "BucketId, BucketUpdateRequest 을 이용하여 버킷을 수정힙니다.")
	@PutMapping("/buckets/{bucketId}")
	public ResponseEntity<Void> modifyBucket(
		@PathVariable(required = true) final Long bucketId,
		@RequestBody @Valid final BucketUpdateRequest request
	){
		bucketService.modifyBucket(bucketId, request.toContent());

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "버킷 삭제", description = "BucketId을 이용하여 버킷을 삭제힙니다.")
	@DeleteMapping("/buckets/{bucketId}")
	public ResponseEntity<Void> deleteBucket(@PathVariable(required = true) final Long bucketId){
		bucketService.deleteBucket(bucketId);

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "버킷 상세 조회", description = "BucketId을 이용하여 버킷을 조회힙니다.")
	@GetMapping("/buckets/{bucketId}")
	public ResponseEntity<GetBucketResponse> getBucket(
		// @PathVariable(required = true) final String nickname,
		@PathVariable(required = true) final Long bucketId
	){
		return ResponseEntity.ok(bucketService.getBucket(bucketId));
	}

	@Operation(summary = "버킷 목록 조회(커서)", description = "유저이름, 취미, 커서 방식 조회 요청을 이용하여 버킷을 조회힙니다.")
	@GetMapping("/{nickname}/buckets/{hobby}")
	public ResponseEntity<GetBucketsByCursorResponse> getBucket(
		@PathVariable(required = true) final String nickname,
		@RequestParam(required = true) final String hobby,
		@RequestBody @Valid BucketGetByCursorRequest request
	){
		GetBucketsByCursorResponse response = bucketService.getBucketsByCursor(
			nickname,
			Hobby.valueOf(hobby),
			request.toParameters()
		);

		return ResponseEntity.ok(response);
	}
}
