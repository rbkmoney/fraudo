package com.rbkmoney.fraudo.resolver.payout;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.Pair;
import com.rbkmoney.fraudo.model.PaymentModel;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.FieldPairResolver;
import com.rbkmoney.fraudo.resolver.FieldValueResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentModelFieldPairResolver implements FieldPairResolver<PaymentModel, CheckedField> {

    private final FieldNameResolver<CheckedField> checkedFieldFieldNameResolver;
    private final FieldValueResolver<PaymentModel> fraudModelFieldValueResolver;

    @Override
    public Pair<CheckedField, String> resolve(String fieldName, PaymentModel paymentModel) {
        return new Pair<>(
                checkedFieldFieldNameResolver.resolve(fieldName),
                fraudModelFieldValueResolver.resolve(fieldName, paymentModel)
        );
    }

}
