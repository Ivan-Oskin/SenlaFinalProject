package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.NotNull;

public class DealRequest {
    @NotNull(message = "The deal must have a adId")
    int adId;
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
