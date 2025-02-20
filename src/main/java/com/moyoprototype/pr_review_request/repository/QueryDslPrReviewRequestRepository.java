package com.moyoprototype.pr_review_request.repository;

import com.moyoprototype.pr_review_request.dto.request.PrReviewRequestsRequestDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestDto;
import org.springframework.data.domain.Slice;

public interface QueryDslPrReviewRequestRepository {

    Slice<PrReviewRequestDto> findAllByStatusAndPositionWithSort(PrReviewRequestsRequestDto requestDto);
}
