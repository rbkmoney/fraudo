package com.rbkmoney.fraudo.payment.visitor;

import com.rbkmoney.fraudo.FraudoParser;

public interface CustomFuncVisitor<T> {

    String visitCountryBy(FraudoParser.Country_byContext ctx, T model);

    boolean visitIn(FraudoParser.InContext ctx, T model);

    boolean visitLike(FraudoParser.LikeContext ctx, T model);

    Integer visitUnique(FraudoParser.UniqueContext ctx, T model);

}
