package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CountVisitorImpl extends FraudoBaseVisitor<Object> {

    private final FraudModel fraudModel;
    private final CountAggregator countAggregator;

    @Override
    public Object visitCount(com.rbkmoney.fraudo.FraudoParser.CountContext ctx) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        String time = TextUtil.safeGetText(ctx.DECIMAL());
        return (double) countAggregator.count(CheckedField.getByValue(countTarget), fraudModel,
                Long.valueOf(time));
    }

    @Override
    public Object visitCount_success(FraudoParser.Count_successContext ctx) {
        String countTarget = ctx.STRING().getText();
        String time = TextUtil.safeGetText(ctx.DECIMAL());
        return (double) countAggregator.countSuccess(CheckedField.getByValue(countTarget), fraudModel,
                Long.valueOf(time));
    }

    @Override
    public Object visitCount_error(FraudoParser.Count_errorContext ctx) {
        String countTarget = TextUtil.safeGetText(ctx.STRING(0));
        String time = TextUtil.safeGetText(ctx.DECIMAL());
        String errorCode = TextUtil.safeGetText(ctx.STRING(1));
        return (double) countAggregator.countError(CheckedField.getByValue(countTarget), fraudModel,
                Long.valueOf(time), errorCode);
    }

}
