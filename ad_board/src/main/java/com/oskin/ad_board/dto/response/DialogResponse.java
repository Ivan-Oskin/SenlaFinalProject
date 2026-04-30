package com.oskin.ad_board.dto.response;

import java.util.List;

public class DialogResponse {
    String adTitle;
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
