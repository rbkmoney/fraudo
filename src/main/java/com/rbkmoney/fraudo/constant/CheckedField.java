package com.rbkmoney.fraudo.constant;

import java.util.HashMap;
import java.util.Map;

public enum CheckedField {

    EMAIL("email"),
    IP("ip"),
    FINGERPRINT("fingerprint"),
    COUNTRY_BANK("country_bank"),
    COUNTRY_IP("country_ip"),
    BIN("bin"),
    PAN("pan"),
    SHOP_ID("shop_id"),
    PARTY_ID("party_id"),
    CARD_TOKEN("card_token");

    private String value;
    private static Map<String, CheckedField> valueMap = new HashMap<>();

    static {
        for (CheckedField value : CheckedField.values()) {
            valueMap.put(value.value, value);
        }
    }

    CheckedField(String value) {
        this.value = value;
    }

    public static CheckedField getByValue(String value) {
        return valueMap.get(value);
    }

}
