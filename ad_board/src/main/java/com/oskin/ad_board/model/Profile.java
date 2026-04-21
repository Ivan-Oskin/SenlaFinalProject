package com.oskin.ad_board.model;

import java.time.LocalDateTime;

public class Profile implements IIdentified {
    private int id;
    private int userId;
    private String name;
    private String surname;
    private int age;
    private int cityId;
    private double rating;
    private int ratingCount;
    private double ratingSum;
    private LocalDateTime createdDateTime;

    public Profile(int id, int userId, String surname, String name, int age, int cityId, double rating, int ratingCount, double ratingSum, LocalDateTime createdDateTime) {
        this.id = id;
        this.userId = userId;
        this.surname = surname;
        this.name = name;
        this.age = age;
        this.cityId = cityId;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.ratingSum = ratingSum;
        this.createdDateTime = createdDateTime;
    }

    public Profile() {
    }

    public Profile(int userId, String name, String surname, int age, double rating, int cityId, int ratingCount, double ratingSum, LocalDateTime createdDateTime) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.rating = rating;
        this.cityId = cityId;
        this.ratingCount = ratingCount;
        this.ratingSum = ratingSum;
        this.createdDateTime = createdDateTime;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public double getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(double ratingSum) {
        this.ratingSum = ratingSum;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
