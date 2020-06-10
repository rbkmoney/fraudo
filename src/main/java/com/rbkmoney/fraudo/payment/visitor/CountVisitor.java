package com.rbkmoney.fraudo.payment.visitor;

import static com.rbkmoney.fraudo.FraudoPaymentParser.*;

public interface CountVisitor<T> {

    Integer visitCount(CountContext ctx, T model);

    Integer visitCountSuccess(Count_successContext ctx, T model);

    Integer visitCountError(Count_errorContext ctx, T model);

    Integer visitCountChargeback(Count_chargebackContext ctx, T model);

    Integer visitCountRefund(Count_refundContext ctx, T model);

}
