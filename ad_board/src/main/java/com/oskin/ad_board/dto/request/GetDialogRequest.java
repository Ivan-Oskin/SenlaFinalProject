package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GetDialogRequest {
    @NotNull(message = "The dialog must have a adId")
    int adId;
    int buyerId;
    @Min(value = 1, message = "the count must be more than 1")
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
