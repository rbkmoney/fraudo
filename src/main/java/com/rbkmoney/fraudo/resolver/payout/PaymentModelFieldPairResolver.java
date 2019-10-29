package com.rbkmoney.fraudo.resolver.payout;

import com.rbkmoney.fraudo.model.Pair;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.FieldPairResolver;
import com.rbkmoney.fraudo.resolver.FieldValueResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentModelFieldPairResolver<T, U> implements FieldPairResolver<T, U> {

    private final FieldNameResolver<U> checkedFieldFieldNameResolver;
    private final FieldValueResolver<T> fraudModelFieldValueResolver;

    @Override
    public Pair<U, String> resolve(String fieldName, T paymentModel) {
        return new Pair<>(
                checkedFieldFieldNameResolver.resolve(fieldName),
                fraudModelFieldValueResolver.resolve(fieldName, paymentModel)
        );
    }

}
