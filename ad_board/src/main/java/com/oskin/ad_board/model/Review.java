package com.oskin.ad_board.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review implements IIdentified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    @Column(name = "rating")
    private int rating;
    @Column(name = "comment")
    private String comment;
    @CreationTimestamp
    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


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

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Review() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Review(User author, Ad ad, int rating, String comment) {
        this.author = author;
        this.ad = ad;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(int id, User author, Ad ad, int rating, String comment) {
        this.id = id;
        this.author = author;
        this.ad = ad;
        this.rating = rating;
        this.comment = comment;
    }
}
