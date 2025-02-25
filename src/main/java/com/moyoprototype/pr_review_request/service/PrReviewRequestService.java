package com.moyoprototype.pr_review_request.service;

import com.moyoprototype.pr_review_request.domain.PrReviewRequest;
import com.moyoprototype.pr_review_request.domain.PrReviewRequestView;
import com.moyoprototype.pr_review_request.dto.request.PrReviewRequestsRequestDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestDetailResponseDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestsResponseDto;
import com.moyoprototype.pr_review_request.repository.PrReviewRequestRepository;
import com.moyoprototype.pr_review_request.repository.PrReviewRequestViewRepository;
import com.moyoprototype.user.domain.User;
import com.moyoprototype.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PrReviewRequestService {

    private final PrReviewRequestRepository prReviewRequestRepository;
    private final UserRepository userRepository;
    private final PrReviewRequestViewRepository prReviewRequestViewRepository;

    public PrReviewRequestsResponseDto getPrReviewRequests(PrReviewRequestsRequestDto requestDto) {

        // 요청에 맞는 status, position 조건의 데이터 조회 + 정렬 DB에서 진행.
        Slice<PrReviewRequestDto> prReviewRequests = prReviewRequestRepository.findAllByStatusAndPositionWithSort(requestDto);

        return PrReviewRequestsResponseDto.of(prReviewRequests.getContent(), prReviewRequests.isLast());
    }

    public PrReviewRequestDetailResponseDto getPrReviewRequestDetail(Long requestId, String username) {
        // 1. PR 리뷰 요청글 조회 (없으면 예외 발생)
        PrReviewRequest prReviewRequest = prReviewRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 PR 리뷰 요청글입니다: " + requestId));

        boolean isWriter = false;

        // 2. 로그인한 사용자인 경우만 조회수 증가 로직 수행
        if (username != null) {
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

            // 3. 조회 기록이 없다면 조회수 증가 및 기록 추가.
            boolean hasViewed = prReviewRequestViewRepository.existsByPrReviewRequestIdAndUserId(requestId, user.getId());

            if (!hasViewed) {
                prReviewRequest.increaseHitCount();

                PrReviewRequestView prReviewRequestView = PrReviewRequestView.builder()
                        .user(user)
                        .prReviewRequest(prReviewRequest)
                        .build();

                prReviewRequestViewRepository.save(prReviewRequestView); // 저장 로직 추가.
            }

            // 4. 현재 사용자가 작성자인지 판별.
            isWriter = prReviewRequest.getUser().equals(user);
        }

        // 5. DTO 변환 후 반환
        return new PrReviewRequestDetailResponseDto(
                prReviewRequest.getStatus() ? "open" : "closed",
                isWriter,
                prReviewRequest.getUser().getProfileImgUrl(),
                prReviewRequest.getUser().getName(),
                prReviewRequest.getPosition(),
                prReviewRequest.getTitle(),
                prReviewRequest.getHitCount(),
                prReviewRequest.getCreatedAt().toString(),
                prReviewRequest.getContent(),
                prReviewRequest.getPrUrl()
        );
    }
}
