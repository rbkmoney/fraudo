package com.rbkmoney.fraudo.payment.visitor;

import com.rbkmoney.fraudo.model.TrustCondition;

import java.util.List;

public interface IsTrustedFuncVisitor<T> {

    boolean visitCheckTrusted(T model);

    boolean visitCheckTrusted(T model, String templateName);

    boolean visitCheckTrusted(T model,
                              List<TrustCondition> paymentsConditionsList,
                              List<TrustCondition> withdrawalConditionsList);
}
