package com.moyoprototype.pr_review_request.repository;

import com.moyoprototype.pr_review_request.domain.PrReviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrReviewRequestRepository extends JpaRepository<PrReviewRequest, Long>, QueryDslPrReviewRequestRepository {
}
