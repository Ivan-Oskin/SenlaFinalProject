package com.oskin.ad_board.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oskin.ad_board.model.ReviewSortType;

import java.time.LocalDateTime;

public class GetReviewRequest {
    ReviewSortType reviewSortType;
    int lastId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime lastDateTime;
    int count;
    double lastRating;

    public double getLastRating() {
        return lastRating;
    }

    public void setLastRating(double lastRating) {
        this.lastRating = lastRating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDateTime getLastDateTime() {
        return lastDateTime;
    }

    public void setLastDateTime(LocalDateTime lastDateTime) {
        this.lastDateTime = lastDateTime;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public GetReviewRequest() {
    }

    public ReviewSortType getReviewSortType() {
        return reviewSortType;
    }

    public void setReviewSortType(ReviewSortType reviewSortType) {
        this.reviewSortType = reviewSortType;
    }
}
