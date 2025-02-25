package com.moyoprototype.pr_review_request.repository;

import com.moyoprototype.pr_review_request.domain.PrReviewRequestView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrReviewRequestViewRepository extends JpaRepository<PrReviewRequestView, Long> {

    boolean existsByPrReviewRequestIdAndUserId(Long prReviewRequestId, Long userId);
}
