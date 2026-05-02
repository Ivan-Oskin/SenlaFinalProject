package com.oskin.ad_board.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oskin.ad_board.model.ReviewSortType;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public class GetReviewRequest {
    ReviewSortType reviewSortType;
    @Min(value = 1, message = "the count must be more than 1")
    int count;
    int lastId;
    double lastRating;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime lastDateTime;

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
