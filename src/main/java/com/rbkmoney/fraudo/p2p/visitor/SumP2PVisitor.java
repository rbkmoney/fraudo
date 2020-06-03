package com.rbkmoney.fraudo.p2p.visitor;

import com.rbkmoney.fraudo.FraudoP2PParser;

public interface SumP2PVisitor<T> {

    Double visitSum(FraudoP2PParser.SumContext ctx, T model);

}
