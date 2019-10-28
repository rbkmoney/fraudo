package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.resolver.GroupByModelResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomFuncVisitorImpl<T extends FraudModel> extends FraudoBaseVisitor<Object> {

    private final T fraudModel;
    private final UniqueValueAggregator uniqueValueAggregator;
    private final CountryResolver countryResolver;

    @Override
    public Object visitCountry_by(FraudoParser.Country_byContext ctx) {
        String fieldName = TextUtil.safeGetText(ctx.STRING());
        String fieldValue = FieldResolver.resolveString(fieldName, fraudModel);
        return countryResolver.resolveCountry(CheckedField.getByValue(fieldName), fieldValue);
    }

    @Override
    public Object visitIn(FraudoParser.InContext ctx) {
        final String fieldValue;
        if (ctx.STRING() != null && ctx.STRING().getText() != null && !ctx.STRING().getText().isEmpty()) {
            String field = TextUtil.safeGetText(ctx.STRING());
            fieldValue = FieldResolver.resolveString(field, fraudModel);
        } else {
            String fieldName = TextUtil.safeGetText(ctx.country_by().STRING());
            String value = FieldResolver.resolveString(fieldName, fraudModel);
            fieldValue = countryResolver.resolveCountry(CheckedField.getByValue(fieldName), value);
        }
        return ctx.string_list().STRING().stream()
                .anyMatch(s -> fieldValue.equals(TextUtil.safeGetText(s)));
    }

    @Override
    public Object visitLike(FraudoParser.LikeContext ctx) {
        String fieldName = TextUtil.safeGetText(ctx.STRING(0));
        String fieldValue = FieldResolver.resolveString(fieldName, fraudModel);
        String pattern = TextUtil.safeGetText(ctx.STRING(1));
        return fieldValue.matches(pattern);
    }

    @Override
    public Object visitUnique(FraudoParser.UniqueContext ctx) {
        String field = TextUtil.safeGetText(ctx.STRING(0));
        String fieldBy = TextUtil.safeGetText(ctx.STRING(1));
        return (double) uniqueValueAggregator.countUniqueValue(CheckedField.getByValue(field), fraudModel,
                CheckedField.getByValue(fieldBy), TimeWindowResolver.resolve(ctx.time_window()), GroupByModelResolver.resolve(ctx.group_by()));
    }

    @Override
    public Object visitAmount(FraudoParser.AmountContext ctx) {
        return (double) fraudModel.getAmount();
    }
}
