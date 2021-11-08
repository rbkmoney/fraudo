package com.rbkmoney.fraudo.test.p2p;

import com.rbkmoney.fraudo.exception.UnresolvableFieldException;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.test.constant.P2PCheckedField;
import com.rbkmoney.fraudo.test.model.P2PModel;

public class P2PModelFieldResolver implements FieldResolver<P2PModel, P2PCheckedField> {

    @Override
    public String resolveValue(String fieldName, P2PModel model) {
        return switch (P2PCheckedField.getByValue(fieldName)) {
            case BIN -> model.getBin();
            case IP -> model.getIp();
            case FINGERPRINT -> model.getFingerprint();
            case EMAIL -> model.getEmail();
            case COUNTRY_BANK -> model.getCountry();
            case CARD_TOKEN_FROM -> model.getCardTokenFrom();
            case CARD_TOKEN_TO -> model.getCardTokenTo();
            case PAN -> model.getPan();
            default -> throw new UnresolvableFieldException(fieldName);
        };
    }

    @Override
    public P2PCheckedField resolveName(String fieldName) {
        return P2PCheckedField.getByValue(fieldName);
    }

}
