package com.moyoprototype.pr_review_request.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class PrReviewRequestDto {

    @JsonProperty("profileImageUrl")
    private String profileImageUrl;

    @JsonProperty("username")
    private String username;

    @JsonProperty("position")
    private String position;

    @JsonProperty("title")
    private String title;

    @JsonProperty("hitCount")
    private int hitCount; // 이건 api 명세서에 빠진 내용임. 추가해야함.

    @JsonProperty("since")
    private String since; // 이건 자체적으로 계산.

    public PrReviewRequestDto(String profileImageUrl, String username, String position, String title, int hitCount, LocalDateTime createdAt) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.position = position;
        this.title = title;
        this.hitCount = hitCount;
        this.since = calculateTimeAgo(createdAt);
    }

    // createdAt과 지금의 간격을 계산.
    private String calculateTimeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();

        // ChronoUnit 인식이 안돼서 Duration으로 대체.
        Duration duration = Duration.between(createdAt, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        long months = days / 30;
        long years = days / 365;

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days < 30) {
            return days + "일 전";
        } else if (months < 12) {
            return months + "달 전";
        } else {
            return years + "년 전";
        }
    }
}
