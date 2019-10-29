package com.rbkmoney.fraudo.resolver.payout;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.exception.UnresolvableFieldException;
import com.rbkmoney.fraudo.model.PaymentModel;
import com.rbkmoney.fraudo.resolver.FieldValueResolver;

public class PaymentModelFieldValueResolver implements FieldValueResolver<PaymentModel> {

    @Override
    public String resolve(String fieldName, PaymentModel paymentModel) {
        switch (CheckedField.getByValue(fieldName)) {
            case BIN:
                return paymentModel.getBin();
            case IP:
                return paymentModel.getIp();
            case FINGERPRINT:
                return paymentModel.getFingerprint();
            case EMAIL:
                return paymentModel.getEmail();
            case COUNTRY_BANK:
                return paymentModel.getBinCountryCode();
            case CARD_TOKEN:
                return paymentModel.getCardToken();
            case PAN:
                return paymentModel.getPan();
            default:
                throw new UnresolvableFieldException(fieldName);
        }
    }

}
