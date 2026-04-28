package com.oskin.ad_board.model;

public enum AdSortType {
    DEFAULT(""),
    PRICE_MIN("price, "),
    PRICE_MAX("price DESC, "),
    CREATE_DATE_TIME("a.createdDateTime DESC, ");

    private final String HqlString;

    AdSortType(String type) {
        this.HqlString = type;
    }

    public String getHqlString() {
        return HqlString;
    }
}
