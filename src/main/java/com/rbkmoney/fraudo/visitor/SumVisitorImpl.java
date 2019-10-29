package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.resolver.payout.GroupByModelResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SumVisitorImpl<T, U> implements SumVisitor<T> {

    private final SumAggregator<T, U> sumAggregator;
    private final FieldNameResolver<U> fieldNameResolver;
    private final GroupByModelResolver<U> groupByModelResolver;

    @Override
    public Double visitSum(FraudoParser.SumContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sum(
                fieldNameResolver.resolve(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupByModelResolver.resolve(ctx.group_by())
        );
    }

    @Override
    public Double visitSumSuccess(FraudoParser.Sum_successContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sumSuccess(
                fieldNameResolver.resolve(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupByModelResolver.resolve(ctx.group_by()));
    }

    @Override
    public Double visitSumError(FraudoParser.Sum_errorContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING(0));
        String errorCode = TextUtil.safeGetText(ctx.STRING(1));
        return sumAggregator.sumError(
                fieldNameResolver.resolve(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                errorCode,
                groupByModelResolver.resolve(ctx.group_by()));
    }

}
