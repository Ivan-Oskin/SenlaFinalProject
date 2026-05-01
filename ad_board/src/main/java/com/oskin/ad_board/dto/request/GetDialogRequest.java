package com.oskin.ad_board.dto.request;

public class GetDialogRequest {
    int adId;
    int buyerId;
    int count;
    int lastId;

    public GetDialogRequest() {
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }
}
