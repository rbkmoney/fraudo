package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.exception.UnresolvableFieldException;
import com.rbkmoney.fraudo.model.FraudModel;

public class FieldResolver {

    public static String resolveString(String fieldName, FraudModel fraudModel) {
        switch (CheckedField.getByValue(fieldName)) {
            case BIN:
                return fraudModel.getBin();
            case IP:
                return fraudModel.getIp();
            case FINGERPRINT:
                return fraudModel.getFingerprint();
            case EMAIL:
                return fraudModel.getEmail();
            case COUNTRY_BANK:
                return fraudModel.getBinCountryCode();
            case CARD_TOKEN:
                return fraudModel.getCardToken();
            case PAN:
                return fraudModel.getPan();
            default:
                throw new UnresolvableFieldException(fieldName);
        }
    }

}
