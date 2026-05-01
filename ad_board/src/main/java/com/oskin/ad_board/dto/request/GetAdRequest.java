package com.oskin.ad_board.dto.request;

import com.oskin.ad_board.model.AdSortType;

public class GetAdRequest {
    private String title;
    private boolean paid;
    private String city;
    private AdSortType adSortType;
    private int count;
    private int page;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public GetAdRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public AdSortType getAdSortType() {
        return adSortType;
    }

    public void setAdSortType(AdSortType adSortType) {
        this.adSortType = adSortType;
    }
}
