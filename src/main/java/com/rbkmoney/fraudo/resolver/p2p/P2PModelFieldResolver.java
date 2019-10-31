package com.rbkmoney.fraudo.resolver.p2p;

import com.rbkmoney.fraudo.constant.P2PCheckedField;
import com.rbkmoney.fraudo.exception.UnresolvableFieldException;
import com.rbkmoney.fraudo.model.P2PModel;
import com.rbkmoney.fraudo.resolver.FieldResolver;

public class P2PModelFieldResolver implements FieldResolver<P2PModel, P2PCheckedField> {

    @Override
    public String resolveValue(String fieldName, P2PModel model) {
        switch (P2PCheckedField.getByValue(fieldName)) {
            case BIN:
                return model.getBin();
            case IP:
                return model.getIp();
            case FINGERPRINT:
                return model.getFingerprint();
            case EMAIL:
                return model.getEmail();
            case COUNTRY_BANK:
                return model.getCountry();
            case CARD_TOKEN_FROM:
                return model.getCardTokenFrom();
            case CARD_TOKEN_TO:
                return model.getCardTokenTo();
            case PAN:
                return model.getPan();
            default:
                throw new UnresolvableFieldException(fieldName);
        }
    }

    @Override
    public P2PCheckedField resolveName(String fieldName) {
        return P2PCheckedField.getByValue(fieldName);
    }

}
