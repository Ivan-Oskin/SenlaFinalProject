package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class MessageRequest {
    @Min(value = 1, message = "the ad id must be more than 1")
    int adId;
    int buyerId;
    @NotBlank(message = "the message cannot be empty")
    String message;

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageRequest() {
    }
}
