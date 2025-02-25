package com.moyoprototype.pr_review_request.controller;

import com.moyoprototype.common.model.ApiResponse;
import com.moyoprototype.oauth.GithubOAuth2User;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestDetailResponseDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestsResponseDto;
import com.moyoprototype.pr_review_request.service.PrReviewRequestService;
import com.moyoprototype.pr_review_request.dto.request.PrReviewRequestsRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/pr-review-requests")
@RequiredArgsConstructor
public class PrReviewRequestController {

    private final PrReviewRequestService prReviewService;

    // 요청글 전체 조회.
    @GetMapping
    public ResponseEntity<ApiResponse<PrReviewRequestsResponseDto>> prReviewRequestList(
            @RequestParam(value = "status", defaultValue = "open") String status,
            @RequestParam(value = "order", defaultValue = "latest_desc") String order,
            @RequestParam(value = "position") String position,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {

//        GithubOAuth2User userPrincipal = (GithubOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("PR리뷰요청글 전체 조회 요청자 : {}", userPrincipal.getUsername());

        PrReviewRequestsRequestDto requestDto = new PrReviewRequestsRequestDto(status, order, position, page, size);

        return ResponseEntity.ok(ApiResponse.success(prReviewService.getPrReviewRequests(requestDto)));
    }

    // 요청글 상세 조회.
    @GetMapping("/{pr-review-requestId}")
    public ResponseEntity<ApiResponse<PrReviewRequestDetailResponseDto>> prReviewRequestDetail(@PathVariable("pr-review-requestId") Long requestId) {

        GithubOAuth2User userPrincipal = (GithubOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("PR리뷰요청글 상세 조회 요청자 : {}", userPrincipal.getUsername());

        return ResponseEntity.ok(ApiResponse.success(prReviewService.getPrReviewRequestDetail(requestId, userPrincipal.getUsername())));
    }

//    // 요청 글 생성.
//    @PostMapping("/")
//    public ResponseEntity<?> create() {
//
//    }
//
//    // 요청 글 수정 폼.
//    @GetMapping("/{pr-review-requestId}/form")
//    public ResponseEntity<?> updateForm() {
//
//    }
//
//    // 요청 글 수정.
//    @PatchMapping("{pr-review-requestId}")
//    public ResponseEntity<?> update(@PathVariable("pr-review-requestId") String parameter) {
//
//    }
//
//    // 요청 글 삭제.
//    @DeleteMapping("{pr-review-requestId}")
//    public ResponseEntity<?> delete(@PathVariable("pr-review-requestId") String parameter) {
//
//    }

    // 내 리뷰 요청글 모아보기 (보류)
}