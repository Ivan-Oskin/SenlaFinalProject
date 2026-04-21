package com.oskin.ad_board.model;

import java.time.LocalDateTime;

public class Ad implements IIdentified {
    private int id;
    private String title;
    private int price;
    private String description;
    private int sellerId;
    private int cityId;
    private LocalDateTime createdDateTime;
    private StatusAd status;
    private boolean IsPaid;

    public Ad(int id, String title, String description, int price, int sellerId, int cityId, LocalDateTime createdDateTime, StatusAd status, boolean isPaid) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sellerId = sellerId;
        this.cityId = cityId;
        this.createdDateTime = createdDateTime;
        this.status = status;
        IsPaid = isPaid;
    }

    public Ad(String title, int price, String description, int sellerId, int cityId, LocalDateTime createdDateTime, StatusAd status, boolean isPaid) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.sellerId = sellerId;
        this.cityId = cityId;
        this.createdDateTime = createdDateTime;
        this.status = status;
        IsPaid = isPaid;
    }

    public Ad() {
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public StatusAd getStatus() {
        return status;
    }

    public void setStatus(StatusAd status) {
        this.status = status;
    }

    public boolean isPaid() {
        return IsPaid;
    }

    public void setPaid(boolean paid) {
        IsPaid = paid;
    }
}
