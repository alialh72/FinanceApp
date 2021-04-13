package com.example.financeapp.Enums;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;

public enum articlesCategoryEnum {

    FINANCE("Finance"),
    TAXES("Taxes"),
    SAVINGS("Savings"),
    INVESTING("Investing"),
    ACCOUNTS("Accounts"),
    CRYPTO("Crypto");

    private final String type;

    articlesCategoryEnum(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static final Map<String, articlesCategoryEnum> LOOKUP = Maps.uniqueIndex(
            Arrays.asList(articlesCategoryEnum.values()),
            articlesCategoryEnum::getType);

}
