package com.oskin.ad_board.dto.response;

import java.util.List;

public class DialogResponse {
    String adTitle;
    int lastId;

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    List<MessageResponse> list;

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public List<MessageResponse> getList() {
        return list;
    }

    public void setList(List<MessageResponse> list) {
        this.list = list;
    }
}
