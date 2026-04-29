package com.oskin.ad_board.dto.request;

import com.oskin.ad_board.model.ReviewSortType;

public class GetReviewRequest {
    ReviewSortType reviewSortType;

    public GetReviewRequest() {
    }

    public ReviewSortType getReviewSortType() {
        return reviewSortType;
    }

    public void setReviewSortType(ReviewSortType reviewSortType) {
        this.reviewSortType = reviewSortType;
    }
}
