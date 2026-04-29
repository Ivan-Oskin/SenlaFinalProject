package com.oskin.ad_board.model;

public enum ReviewSortType {
    RATING_DESC("rating DESC"),
    RATING_ASC("rating"),
    CREATED_DATE_TIME("createdDateTime DESC");

    private final String type;

    public String getType() {
        return type;
    }

    ReviewSortType(String type) {
        this.type = type;
    }
}
