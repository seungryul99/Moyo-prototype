package com.moyoprototype.pr_review_request.dto.response;

public record PrReviewRequestDetailResponseDto(
        String status,
        Boolean isWriter,

        // 채택은 스프린트2에서 추가.
//        Boolean isAdopted,
        String profileImageUrl,
        String username,
        String position,
        String title,
        Integer hitCount,
        String createdAt,
        String content,
        String prUrl
) {

}