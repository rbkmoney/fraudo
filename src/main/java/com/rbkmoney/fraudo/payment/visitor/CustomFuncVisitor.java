package com.rbkmoney.fraudo.payment.visitor;

import static com.rbkmoney.fraudo.FraudoPaymentParser.*;

public interface CustomFuncVisitor<T> {

    String visitCountryBy(Country_byContext ctx, T model);

    boolean visitIn(InContext ctx, T model);

    boolean visitLike(LikeContext ctx, T model);

    Integer visitUnique(UniqueContext ctx, T model);

}
