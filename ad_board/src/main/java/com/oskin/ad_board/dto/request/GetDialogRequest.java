package com.oskin.ad_board.dto.request;

public class GetDialogRequest {
    int adId;
    int buyerId;

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

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }
}
