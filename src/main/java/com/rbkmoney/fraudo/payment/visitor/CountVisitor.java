package com.rbkmoney.fraudo.payment.visitor;

import com.rbkmoney.fraudo.FraudoParser;

public interface CountVisitor<T> {

    Integer visitCount(FraudoParser.CountContext ctx, T model);

    Integer visitCountSuccess(FraudoParser.Count_successContext ctx, T model);

    Integer visitCountError(FraudoParser.Count_errorContext ctx, T model);

    Integer visitCountChargeback(FraudoParser.Count_chargebackContext ctx, T model);

    Integer visitCountRefund(FraudoParser.Count_refundContext ctx, T model);

}
