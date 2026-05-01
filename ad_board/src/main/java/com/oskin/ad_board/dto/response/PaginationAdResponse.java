package com.oskin.ad_board.dto.response;

import java.util.List;

public class PaginationAdResponse {
    int thisPage;

    public int getThisPage() {
        return thisPage;
    }

    public void setThisPage(int thisPage) {
        this.thisPage = thisPage;
    }

    public List<AdResponse> getAdResponseList() {
        return adResponseList;
    }

    public void setAdResponseList(List<AdResponse> adResponseList) {
        this.adResponseList = adResponseList;
    }

    public PaginationAdResponse() {
    }

    List<AdResponse> adResponseList;
}
