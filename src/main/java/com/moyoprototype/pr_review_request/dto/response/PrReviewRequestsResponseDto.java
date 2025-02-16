package com.moyoprototype.pr_review_request.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PrReviewRequestsResponseDto {

    @JsonProperty("prReviewRequestList") // 이 부분 이름 애매 <- API 명세서에 없는 부분임.
    List<PrReviewRequestDto> prReviewRequestList;

    @JsonProperty("isLast")
    private Boolean isLast;

    public static PrReviewRequestsResponseDto of(List<PrReviewRequestDto> prReviewRequestList, Boolean isLast) {
        return new PrReviewRequestsResponseDto(prReviewRequestList, isLast);
    }
}
