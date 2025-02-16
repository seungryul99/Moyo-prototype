package com.moyoprototype.pr_review_request.domain;

import com.moyoprototype.common.model.BaseTimeEntity;
import com.moyoprototype.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pr_review_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrReviewRequest extends BaseTimeEntity {

    // erd에 적힌 필드와 이름이 달라서 erd버리고 api명세대로 감. 뇌정지왔네 순간.

    @Id
    @Column(name = "pr_review_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String prUrl;

    // 직군 태그는 null이 가능하므로 enum 대신 String 선택.
    @Column(nullable = true)
    private String position;

    @Column(nullable = false)
    private int hitCount;

    @Column(nullable = false)
//    private boolean isCompleted; // 접두사 is_ 들어가면 직렬화와 jpa 사용에 헷갈림.
    private boolean status; // 마감 여부.

    @Builder
    public PrReviewRequest(String title, String content, String prUrl, String position, int hitCount, boolean status) {
        this.title = title;
        this.content = content;
        this.prUrl = prUrl;
        this.position = position;
        this.hitCount = hitCount;
        this.status = status;
    }
}
