package com.oskin.ad_board.dto.response;

import java.util.List;

public class PaginationAdModerationResponse {
    int lastId;
    List<AdResponse> adResponseList;

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public List<AdResponse> getAdResponseList() {
        return adResponseList;
    }

    public void setAdResponseList(List<AdResponse> adResponseList) {
        this.adResponseList = adResponseList;
    }
}
