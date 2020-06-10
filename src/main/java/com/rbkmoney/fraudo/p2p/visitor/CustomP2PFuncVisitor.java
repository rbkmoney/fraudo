package com.rbkmoney.fraudo.p2p.visitor;

import com.rbkmoney.fraudo.FraudoP2PParser;

public interface CustomP2PFuncVisitor<T> {

    String visitCountryBy(FraudoP2PParser.Country_byContext ctx, T model);

    boolean visitLike(FraudoP2PParser.LikeContext ctx, T model);

    Integer visitUnique(FraudoP2PParser.UniqueContext ctx, T model);

}
