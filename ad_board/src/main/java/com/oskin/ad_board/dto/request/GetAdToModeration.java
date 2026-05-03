package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.Min;

public class GetAdToModeration {
    @Min(value = 1, message = "the count must be more than 1")
    int count;
    int lastId;

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
}
