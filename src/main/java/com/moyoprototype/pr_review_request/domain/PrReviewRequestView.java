package com.moyoprototype.pr_review_request.domain;

import com.moyoprototype.common.model.BaseTimeEntity;
import com.moyoprototype.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pr_review_request_view", uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_pr", columnNames = {"user_id", "pr_review_request_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrReviewRequestView extends BaseTimeEntity {

    @Id
    @Column(name = "pr_review_request_view_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_review_request_id", nullable = false)
    private PrReviewRequest prReviewRequest;

    @Builder
    public PrReviewRequestView(User user, PrReviewRequest prReviewRequest) {
        this.user = user;
        this.prReviewRequest = prReviewRequest;
    }


}
