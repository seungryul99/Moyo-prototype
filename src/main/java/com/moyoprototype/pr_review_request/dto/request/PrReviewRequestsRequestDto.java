package com.moyoprototype.pr_review_request.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PrReviewRequestsRequestDto {

    private String status;
    private String order;
    private String position;
    private int page;
    private int size;

    // status를 boolean으로 변환.
//    public Boolean getStatusAsBoolean() {
//        return "open".equalsIgnoreCase(status); // "open"이면 true, "closed"면 false.
//    }
    public Boolean getStatus() {
        return "open".equalsIgnoreCase(status); // "open"이면 true, 아니면 false.
    }


//      // 아래처럼 쿼리 파라미터가 많아서 컨트롤러 가독성을 높이기 위해 DTO화.
//
//      public ResponseEntity<List<PrReviewRequestResponseDto>> prReviewRequestList(
//                  @RequestParam(value = "status", defaultValue = "open") String status,
//                  @RequestParam(value = "order", defaultValue = "latest_desc") String order,
//                  @RequestParam(value = "position") String position,
//                  @RequestParam(value = "page") int page,
//                  @RequestParam(value = "size") int size
//          )
//
}
