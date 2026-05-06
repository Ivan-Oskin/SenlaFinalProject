package com.oskin.ad_board.dto.request;


import jakarta.validation.constraints.Min;

public class DealRequest {
    @Min(value = 1, message = "the ad id must be more than 1")
    int adId;
    @Min(value = 1, message = "the buyer id must be more than 1")
    int buyerId;

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

    public DealRequest() {
    }
}
