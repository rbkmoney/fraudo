package com.rbkmoney.fraudo.visitor.impl;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.GroupFieldsResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import com.rbkmoney.fraudo.visitor.CountVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CountVisitorImpl<T, U> implements CountVisitor<T> {

    private final CountAggregator<T, U> countAggregator;
    private final FieldNameResolver<U> fieldNameResolver;
    private final GroupFieldsResolver<U> groupFieldsResolver;

    @Override
    public Integer visitCount(com.rbkmoney.fraudo.FraudoParser.CountContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return countAggregator.count(
                fieldNameResolver.resolve(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupFieldsResolver.resolve(ctx.group_by())
        );
    }

    @Override
    public Integer visitCountSuccess(FraudoParser.Count_successContext ctx, T model) {
        String countTarget = ctx.STRING().getText();
        return countAggregator.countSuccess(
                fieldNameResolver.resolve(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                groupFieldsResolver.resolve(ctx.group_by())
        );
    }

    @Override
    public Integer visitCountError(FraudoParser.Count_errorContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING(0));
        String errorCode = TextUtil.safeGetText(ctx.STRING(1));
        return countAggregator.countError(
                fieldNameResolver.resolve(countTarget),
                model,
                TimeWindowResolver.resolve(ctx.time_window()),
                errorCode,
                groupFieldsResolver.resolve(ctx.group_by())
        );
    }

}
