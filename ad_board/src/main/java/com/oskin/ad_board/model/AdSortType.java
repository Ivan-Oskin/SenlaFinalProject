package com.oskin.ad_board.model;

public enum AdSortType {
    DEFAULT(""),
    PRICE_ASC("price, "),
    PRICE_DESC("price DESC, "),
    CREATE_DATE_TIME("a.createdDateTime DESC, ");

    private final String HqlString;

    AdSortType(String type) {
        this.HqlString = type;
    }

    public String getHqlString() {
        return HqlString;
    }
}
