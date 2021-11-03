package com.rbkmoney.fraudo.payment.resolver;

import com.rbkmoney.fraudo.model.TrustCondition;

import java.util.List;

public interface CustomerTypeResolver<T> {

    Boolean isTrusted(T model);

    Boolean isTrusted(List<TrustCondition> paymentsConditions, List<TrustCondition> withdrawalsConditions);

}
