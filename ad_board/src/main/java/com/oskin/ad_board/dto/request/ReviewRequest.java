package com.oskin.ad_board.dto.request;

public class ReviewRequest {
    int rating;
    String comment;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ReviewRequest() {
    }
}
