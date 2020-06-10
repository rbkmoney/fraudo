package com.rbkmoney.fraudo.payment.visitor.impl;

import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.model.Pair;
import com.rbkmoney.fraudo.payment.resolver.PaymentGroupResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTimeWindowResolver;
import com.rbkmoney.fraudo.payment.visitor.CustomFuncVisitor;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

import static com.rbkmoney.fraudo.FraudoPaymentParser.*;

@RequiredArgsConstructor
public class CustomFuncVisitorImpl<T, U> implements CustomFuncVisitor<T> {

    private final UniqueValueAggregator<T, U> uniqueValueAggregator;
    private final CountryResolver<U> countryResolver;
    private final FieldResolver<T, U> fieldResolver;
    private final PaymentGroupResolver<T, U> groupResolver;
    private final PaymentTimeWindowResolver timeWindowResolver;

    @Override
    public String visitCountryBy(Country_byContext ctx, T model) {
        String fieldName = TextUtil.safeGetText(ctx.STRING());
        Pair<U, String> resolve = fieldResolver.resolve(fieldName, model);
        return countryResolver.resolveCountry(resolve.getFirst(), resolve.getSecond());
    }

    @Override
    public boolean visitLike(LikeContext ctx, T model) {
        String fieldName = TextUtil.safeGetText(ctx.STRING(0));
        String fieldValue = fieldResolver.resolve(fieldName, model).getSecond();
        String pattern = TextUtil.safeGetText(ctx.STRING(1));
        return fieldValue.matches(pattern);
    }

    @Override
    public Integer visitUnique(UniqueContext ctx, T model) {
        String field = TextUtil.safeGetText(ctx.STRING(0));
        String fieldBy = TextUtil.safeGetText(ctx.STRING(1));
        return uniqueValueAggregator.countUniqueValue(
                fieldResolver.resolveName(field),
                model,
                fieldResolver.resolveName(fieldBy),
                timeWindowResolver.resolve(ctx.time_window()),
                groupResolver.resolve(ctx.group_by())
        );
    }

}
