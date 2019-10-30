package com.rbkmoney.fraudo.visitor.impl;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.model.Pair;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.resolver.GroupByModelResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import com.rbkmoney.fraudo.visitor.CustomFuncVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomFuncVisitorImpl<T, U> implements CustomFuncVisitor<T> {

    private final UniqueValueAggregator<T, U> uniqueValueAggregator;
    private final CountryResolver<U> countryResolver;
    private final FieldResolver<T, U> fieldResolver;
    private final GroupByModelResolver<T, U> groupByModelResolver;

    @Override
    public String visitCountryBy(FraudoParser.Country_byContext ctx, T model) {
        String fieldName = TextUtil.safeGetText(ctx.STRING());
        Pair<U, String> resolve = fieldResolver.resolve(fieldName, model);
        return countryResolver.resolveCountry(resolve.getFirst(), resolve.getSecond());
    }

    @Override
    public boolean visitIn(FraudoParser.InContext ctx, T model) {
        final String fieldValue;
        if (ctx.STRING() != null && ctx.STRING().getText() != null && !ctx.STRING().getText().isEmpty()) {
            String field = TextUtil.safeGetText(ctx.STRING());
            fieldValue = fieldResolver.resolve(field, model).getSecond();
        } else {
            String fieldName = TextUtil.safeGetText(ctx.country_by().STRING());
            Pair<U, String> resolve = fieldResolver.resolve(fieldName, model);
            fieldValue = countryResolver.resolveCountry(resolve.getFirst(), resolve.getSecond());
        }
        return ctx.string_list().STRING().stream()
                .anyMatch(s -> fieldValue.equals(TextUtil.safeGetText(s)));
    }

    @Override
    public boolean visitLike(FraudoParser.LikeContext ctx, T model) {
        String fieldName = TextUtil.safeGetText(ctx.STRING(0));
        String fieldValue = fieldResolver.resolve(fieldName, model).getSecond();
        String pattern = TextUtil.safeGetText(ctx.STRING(1));
        return fieldValue.matches(pattern);
    }

    @Override
    public Integer visitUnique(FraudoParser.UniqueContext ctx, T model) {
        String field = TextUtil.safeGetText(ctx.STRING(0));
        String fieldBy = TextUtil.safeGetText(ctx.STRING(1));
        return uniqueValueAggregator.countUniqueValue(
                fieldResolver.resolveName(field),
                model,
                fieldResolver.resolveName(fieldBy),
                TimeWindowResolver.resolve(ctx.time_window()),
                groupByModelResolver.resolve(ctx.group_by())
        );
    }

}
