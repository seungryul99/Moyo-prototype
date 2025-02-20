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

        Optional<PrReviewRequest> prReviewRequestDetail = prReviewRequestRepository.findById(requestId);

        // 잘못된 요청글 id라면 예외 발생.
//        if (prReviewRequestDetail.isEmpty());

        boolean isWriter = false;

        if (username != null) {
            Optional<User> userOptional = userRepository.findByName(username);

            Long userId = userOptional.map(User::getId).orElse(null);

            // 유저네임이 있다면 유저 Id는 무조건 존재함. 아래 로직은 필요한 예외가 있나?
            if (userId != null) {
                boolean hasViewed = prReviewRequestViewRepository.existsByPrReviewRequestIdAndUserId(requestId, userId);

                // 회원 1명당 조회수 1개만 증가 가능.
                if (!hasViewed) {
                    prReviewRequestDetail.get().increaseHitCount();

                    PrReviewRequestView prReviewRequestView = PrReviewRequestView.builder()
                            .user(userOptional.get())
                            .prReviewRequest(prReviewRequestDetail.get())
                            .build();
                }

                // 현재 사용자가 작성자인지 판별.
                if (prReviewRequestDetail.get().getUser().getId().equals(userId)) {
                    isWriter = true;
                }
            }
        }

        PrReviewRequest prReviewRequest = prReviewRequestDetail.get();

        return new PrReviewRequestDetailResponseDto(
                prReviewRequest.getStatus() ? "open" : "closed",
                isWriter,
                prReviewRequest.getUser().getProfileImgUrl(),
                prReviewRequest.getUser().getName(), // username임.
                prReviewRequest.getPosition(),
                prReviewRequest.getTitle(),
                prReviewRequest.getHitCount(), // 위의 증가 로직이 반영된 결과를 가져가야함.
                prReviewRequest.getCreatedAt().toString(),
                prReviewRequest.getContent(),
                prReviewRequest.getPrUrl()
        );
    }
}
