package com.oskin.ad_board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oskin.ad_board.model.StatusAd;

import java.time.LocalDateTime;

public class AdResponse {
    int id;
    String title;
    int price;
    String description;
    int seller_id;
    String city;
    @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    LocalDateTime createdDateTime;
    StatusAd statusAd;

    public AdResponse() {
    }

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

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public StatusAd getStatusAd() {
        return statusAd;
    }

    public void setStatusAd(StatusAd statusAd) {
        this.statusAd = statusAd;
    }
}
