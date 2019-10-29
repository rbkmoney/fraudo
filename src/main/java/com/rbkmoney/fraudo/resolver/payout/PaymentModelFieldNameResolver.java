package com.rbkmoney.fraudo.resolver.payout;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentModelFieldNameResolver implements FieldNameResolver<CheckedField> {

    @Override
    public CheckedField resolve(String fieldName) {
        return CheckedField.getByValue(fieldName);
    }

}
