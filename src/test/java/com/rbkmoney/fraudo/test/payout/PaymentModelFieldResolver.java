package com.rbkmoney.fraudo.test.payout;

import com.rbkmoney.fraudo.exception.UnresolvableFieldException;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.test.constant.PaymentCheckedField;
import com.rbkmoney.fraudo.test.model.PaymentModel;

public class PaymentModelFieldResolver implements FieldResolver<PaymentModel, PaymentCheckedField> {

    @Override
    public String resolveValue(String fieldName, PaymentModel paymentModel) {
        switch (PaymentCheckedField.getByValue(fieldName)) {
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

    @Override
    public PaymentCheckedField resolveName(String fieldName) {
        return PaymentCheckedField.getByValue(fieldName);
    }

}
