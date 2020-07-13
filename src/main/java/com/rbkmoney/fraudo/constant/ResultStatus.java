package com.rbkmoney.fraudo.constant;

import java.util.HashMap;
import java.util.Map;

public enum ResultStatus {

    ACCEPT("accept"),
    ACCEPT_AND_NOTIFY("acceptAndNotify"),
    THREE_DS("3ds"),
    DECLINE("decline"),
    DECLINE_AND_NOTIFY("declineAndNotify"),
    HIGH_RISK("highRisk"),
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
