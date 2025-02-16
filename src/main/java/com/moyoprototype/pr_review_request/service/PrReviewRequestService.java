package com.moyoprototype.pr_review_request.service;

import com.moyoprototype.pr_review_request.dto.request.PrReviewRequestsRequestDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestsResponseDto;
import com.moyoprototype.pr_review_request.repository.PrReviewRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PrReviewRequestService {

    private final PrReviewRequestRepository prReviewRequestRepository;

    public PrReviewRequestsResponseDto getPrReviewRequests(PrReviewRequestsRequestDto requestDto) {

        // 요청에 맞는 status, position 조건의 데이터 조회 + 정렬 DB에서 진행.
        Slice<PrReviewRequestDto> prReviewRequests = prReviewRequestRepository.findAllByStatusAndPositionWithSort(requestDto);

        return PrReviewRequestsResponseDto.of(prReviewRequests.getContent(), prReviewRequests.isLast());
    }
}
