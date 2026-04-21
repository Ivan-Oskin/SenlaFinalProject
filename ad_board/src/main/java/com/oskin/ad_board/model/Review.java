package com.oskin.ad_board.model;

import java.time.LocalDateTime;

public class Review implements IIdentified {
    private int id;
    private int authorId;
    private int adId;
    private int rating;
    private String comment;
    private LocalDateTime createdDateTime;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Review() {
    }

    public Review(int authorId, int adId, int rating, String comment, LocalDateTime createdDateTime) {
        this.authorId = authorId;
        this.adId = adId;
        this.rating = rating;
        this.comment = comment;
        this.createdDateTime = createdDateTime;
    }

    public Review(int id, int authorId, int adId, int rating, String comment, LocalDateTime createdDateTime) {
        this.id = id;
        this.authorId = authorId;
        this.adId = adId;
        this.rating = rating;
        this.comment = comment;
        this.createdDateTime = createdDateTime;
    }
}
