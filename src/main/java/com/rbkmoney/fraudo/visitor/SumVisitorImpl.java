package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.resolver.GroupByModelResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SumVisitorImpl extends FraudoBaseVisitor<Object> {

    private final FraudModel fraudModel;
    private final SumAggregator sumAggregator;

    @Override
    public Object visitSum(FraudoParser.SumContext ctx) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sum(CheckedField.getByValue(countTarget), fraudModel,
                TimeWindowResolver.resolve(ctx.time_window()), GroupByModelResolver.resolve(ctx.group_by()));
    }

    @Override
    public Object visitSum_success(FraudoParser.Sum_successContext ctx) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sumSuccess(CheckedField.getByValue(countTarget), fraudModel,
                TimeWindowResolver.resolve(ctx.time_window()), GroupByModelResolver.resolve(ctx.group_by()));
    }

    @Override
    public Object visitSum_error(FraudoParser.Sum_errorContext ctx) {
        String countTarget = TextUtil.safeGetText(ctx.STRING(0));
        String errorCode = TextUtil.safeGetText(ctx.STRING(1));
        return sumAggregator.sumError(CheckedField.getByValue(countTarget), fraudModel,
                TimeWindowResolver.resolve(ctx.time_window()), errorCode, GroupByModelResolver.resolve(ctx.group_by()));
    }

}
