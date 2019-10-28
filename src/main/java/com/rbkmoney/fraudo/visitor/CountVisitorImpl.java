package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.resolver.GroupByModelResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CountVisitorImpl<T> extends FraudoBaseVisitor<Double> {

    private final T fraudModel;
    private final CountAggregator<T> countAggregator;

    @Override
    public Double visitCount(com.rbkmoney.fraudo.FraudoParser.CountContext ctx) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return Double.valueOf(countAggregator.count(CheckedField.getByValue(countTarget), fraudModel,
                TimeWindowResolver.resolve(ctx.time_window()), GroupByModelResolver.resolve(ctx.group_by())));
    }

    @Override
    public Double visitCount_success(FraudoParser.Count_successContext ctx) {
        String countTarget = ctx.STRING().getText();
        return Double.valueOf(countAggregator.countSuccess(CheckedField.getByValue(countTarget), fraudModel,
                TimeWindowResolver.resolve(ctx.time_window()), GroupByModelResolver.resolve(ctx.group_by())));
    }

    @Override
    public Double visitCount_error(FraudoParser.Count_errorContext ctx) {
        String countTarget = TextUtil.safeGetText(ctx.STRING(0));
        String errorCode = TextUtil.safeGetText(ctx.STRING(1));
        return Double.valueOf(countAggregator.countError(CheckedField.getByValue(countTarget), fraudModel,
                TimeWindowResolver.resolve(ctx.time_window()), errorCode, GroupByModelResolver.resolve(ctx.group_by())));
    }

}
