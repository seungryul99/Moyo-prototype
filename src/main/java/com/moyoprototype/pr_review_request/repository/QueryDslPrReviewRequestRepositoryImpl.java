package com.moyoprototype.pr_review_request.repository;

import com.moyoprototype.pr_review_request.domain.QPrReviewRequest;
import com.moyoprototype.pr_review_request.dto.request.PrReviewRequestsRequestDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryDslPrReviewRequestRepositoryImpl implements QueryDslPrReviewRequestRepository {

//    private static final int REQUEST_LIST_SIZE = 20;

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PrReviewRequestDto> findAllByStatusAndPositionWithSort(PrReviewRequestsRequestDto requestDto) {

        QPrReviewRequest qPrReviewRequest = QPrReviewRequest.prReviewRequest;

        var query = queryFactory.selectFrom(qPrReviewRequest)
                .where(qPrReviewRequest.status.eq(requestDto.getStatusAsBoolean())
                        .and(qPrReviewRequest.position.eq(requestDto.getPosition())));

        String order = requestDto.getOrder();

        // 요청별 정렬 처리.
        if ("latest_desc".equals(order)) {
            query = query.orderBy(qPrReviewRequest.createdAt.desc());
        } else if ("latest_asc".equals(order)) {
            query = query.orderBy(qPrReviewRequest.createdAt.asc());
        } else if ("hits_desc".equals(order)) {
            query = query.orderBy(qPrReviewRequest.hitCount.desc());
        } else if ("hits_asc".equals(order)) {
            query = query.orderBy(qPrReviewRequest.hitCount.asc());
        }

        int page = requestDto.getPage();
        int size = requestDto.getSize();

        // 슬라이스 처리.
        List<PrReviewRequestDto> prReviewRequests = query.offset(page * size)
                .limit(size + 1) // 마지막 페이지 여부를 확인하기 위해 하나 더 가져옴.
                .select(Projections.constructor(PrReviewRequestDto.class,
                        qPrReviewRequest.user.profileImgUrl.as("profileImageUrl"),
                        qPrReviewRequest.user.name.as("username"),
                        qPrReviewRequest.position,
                        qPrReviewRequest.title,
                        qPrReviewRequest.hitCount,
                        qPrReviewRequest.createdAt
                        ))
                .fetch();

        // 마지막인지 확인.
        boolean hasNext = prReviewRequests.size() > size;
        if (hasNext) {
            prReviewRequests.removeLast();
        }

        return new SliceImpl<>(prReviewRequests, PageRequest.of(page, size), hasNext);
    }
}
