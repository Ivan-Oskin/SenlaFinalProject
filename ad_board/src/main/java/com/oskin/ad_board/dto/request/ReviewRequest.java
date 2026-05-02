package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ReviewRequest {
    @Min(value = 1, message = "the rating should be between 1 and 5")
    @Max(value = 5, message = "the rating should be between 1 and 5")
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
