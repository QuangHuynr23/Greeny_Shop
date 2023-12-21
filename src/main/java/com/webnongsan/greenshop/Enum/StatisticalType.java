package com.webnongsan.greenshop.Enum;

public enum StatisticalType {
    Day("day"),
    Other("other");
    private final String value;

    StatisticalType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
