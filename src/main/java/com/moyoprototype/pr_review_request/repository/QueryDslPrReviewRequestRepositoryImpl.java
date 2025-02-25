package com.moyoprototype.pr_review_request.repository;

import com.moyoprototype.pr_review_request.domain.QPrReviewRequest;
import com.moyoprototype.pr_review_request.dto.request.PrReviewRequestsRequestDto;
import com.moyoprototype.pr_review_request.dto.response.PrReviewRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryDslPrReviewRequestRepositoryImpl implements QueryDslPrReviewRequestRepository {

//    private static final int REQUEST_LIST_SIZE = 20;

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PrReviewRequestDto> findAllByStatusAndPositionWithSort(PrReviewRequestsRequestDto requestDto) {

        QPrReviewRequest qPrReviewRequest = QPrReviewRequest.prReviewRequest;

        // BooleanBuilder를 사용하여 동적으로 조건을 추가
        BooleanBuilder whereClause = new BooleanBuilder();

        if (requestDto.getStatus() != null) {
            whereClause.and(qPrReviewRequest.status.eq(requestDto.getStatus()));
        }
        if (requestDto.getPosition() != null) {
            whereClause.and(qPrReviewRequest.position.eq(requestDto.getPosition()));
        }

        // QueryDSL Query 생성
        var query = queryFactory.selectFrom(qPrReviewRequest)
                .where(whereClause); // 조건 추가

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

        // 슬라이스 처리 - 직접 생성자 호출(컴파일 타임에 에러 감지)
        List<PrReviewRequestDto> prReviewRequests = query.offset(page * size)
                .limit(size + 1)
                .select(Projections.constructor(PrReviewRequestDto.class,
                        qPrReviewRequest.user.profileImgUrl,
                        qPrReviewRequest.user.name,
                        qPrReviewRequest.position,
                        qPrReviewRequest.title,
                        qPrReviewRequest.hitCount,
                        qPrReviewRequest.createdAt))
                .fetch();

//        // 슬라이스 처리 - 리플렉션 사용(런타임)
//        List<PrReviewRequestDto> prReviewRequests = query.offset(page * size)
//                .limit(size + 1) // 마지막 페이지 여부를 확인하기 위해 하나 더 가져옴.
//                .select(Projections.constructor(PrReviewRequestDto.class,
//                        qPrReviewRequest.user.profileImgUrl.as("profileImageUrl"),
//                        qPrReviewRequest.user.name.as("username"),
//                        qPrReviewRequest.position,
//                        qPrReviewRequest.title,
//                        qPrReviewRequest.hitCount,
//                        qPrReviewRequest.createdAt
//                        ))
//                .fetch();

        // 마지막인지 확인.
        boolean hasNext = prReviewRequests.size() > size;
        if (hasNext) {
//            prReviewRequests.removeLast();
            prReviewRequests.remove(prReviewRequests.size() - 1); // 마지막 요소 제거
        }

        return new SliceImpl<>(prReviewRequests, PageRequest.of(page, size), hasNext);
    }
}
