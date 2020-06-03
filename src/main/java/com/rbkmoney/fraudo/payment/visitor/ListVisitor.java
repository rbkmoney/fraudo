package com.rbkmoney.fraudo.payment.visitor;

import com.rbkmoney.fraudo.FraudoParser;

public interface ListVisitor<T> {

    Boolean visitInWhiteList(FraudoParser.In_white_listContext ctx, T model);

    Boolean visitInBlackList(FraudoParser.In_black_listContext ctx, T model);

    Boolean visitInGreyList(FraudoParser.In_grey_listContext ctx, T model);

    Boolean visitInList(FraudoParser.In_listContext ctx, T model);

}
