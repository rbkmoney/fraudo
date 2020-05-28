package com.rbkmoney.fraudo.visitor.impl;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.resolver.GroupByModelResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import com.rbkmoney.fraudo.visitor.SumVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SumVisitorImpl<T, U> implements SumVisitor<T> {

    private final SumAggregator<T, U> sumAggregator;
    private final FieldResolver<T, U> fieldResolver;
    private final GroupByModelResolver<T, U> groupByModelResolver;

    @Override
    public Double visitSum(FraudoParser.SumContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sum(
                fieldResolver.resolveName(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupByModelResolver.resolve(ctx.group_by())
        );
    }

    @Override
    public Double visitSumSuccess(FraudoParser.Sum_successContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sumSuccess(
                fieldResolver.resolveName(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupByModelResolver.resolve(ctx.group_by()));
    }

    @Override
    public Double visitSumError(FraudoParser.Sum_errorContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING(0));
        String errorCode = TextUtil.safeGetText(ctx.STRING(1));
        return sumAggregator.sumError(
                fieldResolver.resolveName(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                errorCode,
                groupByModelResolver.resolve(ctx.group_by()));
    }

    @Override
    public Double visitSumChargeback(FraudoParser.Sum_chargebackContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sumChargeback(
                fieldResolver.resolveName(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupByModelResolver.resolve(ctx.group_by())
        );
    }

    @Override
    public Double visitSumRefund(FraudoParser.Sum_refundContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sumRefund(
                fieldResolver.resolveName(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupByModelResolver.resolve(ctx.group_by())
        );
    }

}
