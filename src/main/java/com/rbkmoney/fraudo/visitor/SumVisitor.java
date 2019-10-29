package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoParser;

public interface SumVisitor<T> {

    Double visitSum(FraudoParser.SumContext ctx, T model);

    Double visitSumSuccess(FraudoParser.Sum_successContext ctx, T model);

    Double visitSumError(FraudoParser.Sum_errorContext ctx, T model);

}
