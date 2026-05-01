package com.oskin.ad_board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class PaginationReviewResponse {
    List<ReviewResponse> list;
    int lastId;
    double lastRating;

    public PaginationReviewResponse() {
    }

    public LocalDateTime getLastDateTime() {
        return lastDateTime;
    }

    public void setLastDateTime(LocalDateTime lastDateTime) {
        this.lastDateTime = lastDateTime;
    }

    public double getLastRating() {
        return lastRating;
    }

    public void setLastRating(double lastRating) {
        this.lastRating = lastRating;
    }

    public List<ReviewResponse> getList() {
        return list;
    }

    public void setList(List<ReviewResponse> list) {
        this.list = list;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime lastDateTime;

}
