package com.oskin.ad_board.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oskin.ad_board.model.ReviewSortType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public class GetReviewRequest {
    private ReviewSortType reviewSortType;
    @Min(value = 1, message = "the count must be more than 1")
    private int count;
    private int lastId;
    private int lastRating;
    @Schema(type = "string", example = "2026-05-12T16:10:53", format = "date-time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastDateTime;

    public int getLastRating() {
        return lastRating;
    }

    public void setLastRating(int lastRating) {
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
        if (reviewSortType == null) {
            reviewSortType = ReviewSortType.RATING_DESC;
        }
        return reviewSortType;
    }

    public void setReviewSortType(ReviewSortType reviewSortType) {
        this.reviewSortType = reviewSortType;
    }
}
