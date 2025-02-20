package com.moyoprototype.pr_review_request.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PrReviewRequestsResponseDto {

    List<PrReviewRequestDto> prReviewRequestList;

    private Boolean isLast;

    public static PrReviewRequestsResponseDto of(List<PrReviewRequestDto> prReviewRequestList, Boolean isLast) {
        return new PrReviewRequestsResponseDto(prReviewRequestList, isLast);
    }
}
