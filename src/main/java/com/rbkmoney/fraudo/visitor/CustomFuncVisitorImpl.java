package com.rbkmoney.fraudo.visitor;

import com.rbkmoney.fraudo.FraudoBaseVisitor;
import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;

@RequiredArgsConstructor
public class CustomFuncVisitorImpl extends FraudoBaseVisitor<Object> {

    private final FraudModel fraudModel;
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
        String field = TextUtil.safeGetText(ctx.STRING());
        String fieldValue = FieldResolver.resolveString(field, fraudModel);
        for (TerminalNode string : ctx.string_list().STRING()) {
            if (fieldValue.equals(TextUtil.safeGetText(string))) {
                return true;
            }
        }
        return false;
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
                CheckedField.getByValue(fieldBy));
    }

    @Override
    public Object visitAmount(FraudoParser.AmountContext ctx) {
        return (double) fraudModel.getAmount();
    }
}
