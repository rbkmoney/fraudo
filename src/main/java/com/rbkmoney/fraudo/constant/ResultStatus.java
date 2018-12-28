package com.rbkmoney.fraudo.constant;

import java.util.HashMap;
import java.util.Map;

public enum ResultStatus {

    ACCEPT("accept"),
    THREE_DS("3ds"),
    DECLINE("decline"),
    NORMAL("normal"),
    NOTIFY("notify");

    private String value;
    private static Map<String, ResultStatus> valueMap = new HashMap<>();

    static {
        for (ResultStatus value : ResultStatus.values()) {
            valueMap.put(value.value, value);
        }
    }

    ResultStatus(String value) {
        this.value = value;
    }

    public static ResultStatus getByValue(String value) {
        return valueMap.get(value);
    }

}
