package com.oskin.ad_board.model;

public class Dialog implements IIdentified {
    private int id;
    private int adId;
    private int buyerId;

    public Dialog() {
    }

    public Dialog(int adId, int buyerId) {
        this.adId = adId;
        this.buyerId = buyerId;
    }

    public Dialog(int id, int adId, int buyerId) {
        this.id = id;
        this.adId = adId;
        this.buyerId = buyerId;
    }

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
}
