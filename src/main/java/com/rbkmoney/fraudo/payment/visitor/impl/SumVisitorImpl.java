package com.rbkmoney.fraudo.payment.visitor.impl;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.payment.aggregator.SumPaymentAggregator;
import com.rbkmoney.fraudo.payment.resolver.PaymentGroupResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTimeWindowResolver;
import com.rbkmoney.fraudo.payment.visitor.SumVisitor;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SumVisitorImpl<T, U> implements SumVisitor<T> {

    private final SumPaymentAggregator<T, U> sumPaymentAggregator;
    private final FieldResolver<T, U> fieldResolver;
    private final PaymentGroupResolver<T, U> groupResolver;
    private final PaymentTimeWindowResolver timeWindowResolver;

    @Override
    public Double visitSum(FraudoParser.SumContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumPaymentAggregator.sum(
                fieldResolver.resolveName(countTarget),
                model,
                timeWindowResolver.resolve(ctx.time_window()),
                groupResolver.resolve(ctx.group_by())
        );
    }

    @Override
    public Double visitSumSuccess(FraudoParser.Sum_successContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumPaymentAggregator.sumSuccess(
                fieldResolver.resolveName(countTarget),
                model,
                timeWindowResolver.resolve(ctx.time_window()),
                groupResolver.resolve(ctx.group_by()));
    }

    @Override
    public Double visitSumError(FraudoParser.Sum_errorContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING(0));
        String errorCode = TextUtil.safeGetText(ctx.STRING(1));
        return sumPaymentAggregator.sumError(
                fieldResolver.resolveName(countTarget),
                model,
                timeWindowResolver.resolve(ctx.time_window()),
                errorCode,
                groupResolver.resolve(ctx.group_by()));
    }

    @Override
    public Double visitSumChargeback(FraudoParser.Sum_chargebackContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumPaymentAggregator.sumChargeback(
                fieldResolver.resolveName(countTarget),
                model,
                timeWindowResolver.resolve(ctx.time_window()),
                groupResolver.resolve(ctx.group_by())
        );
    }

    @Override
    public Double visitSumRefund(FraudoParser.Sum_refundContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumPaymentAggregator.sumRefund(
                fieldResolver.resolveName(countTarget),
                model,
                timeWindowResolver.resolve(ctx.time_window()),
                groupResolver.resolve(ctx.group_by())
        );
    }

}
