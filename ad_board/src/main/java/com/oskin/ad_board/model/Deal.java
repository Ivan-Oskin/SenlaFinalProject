package com.oskin.ad_board.model;

import java.time.LocalDateTime;

public class Deal implements IIdentified{
    private int id;
    private int adId;
    private int buyerId;
    private StatusDeal status;
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

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public StatusDeal getStatus() {
        return status;
    }

    public void setStatus(StatusDeal status) {
        this.status = status;
    }

    public Deal() {
    }

    public Deal(int adId, int buyerId, StatusDeal status, LocalDateTime createdDateTime) {
        this.adId = adId;
        this.buyerId = buyerId;
        this.status = status;
        this.createdDateTime = createdDateTime;
    }

    public Deal(int id, int adId, int buyerId, StatusDeal status, LocalDateTime createdDateTime) {
        this.id = id;
        this.adId = adId;
        this.buyerId = buyerId;
        this.status = status;
        this.createdDateTime = createdDateTime;
    }
}
