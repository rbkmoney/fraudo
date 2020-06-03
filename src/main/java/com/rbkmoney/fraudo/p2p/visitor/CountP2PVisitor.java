package com.rbkmoney.fraudo.p2p.visitor;

import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.FraudoParser;

public interface CountP2PVisitor<T> {

    Integer visitCount(FraudoP2PParser.CountContext ctx, T model);

}
