package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountKeyGenerator {

    public static <T> String generate(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.CountContext ctx = (FraudoParser.CountContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateSuccessKey(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.Count_successContext ctx = (FraudoParser.Count_successContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateErrorKey(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.Count_errorContext ctx = (FraudoParser.Count_errorContext) context;
        return CommonKeyGenerator.generateKeyGroupedTwoFieldFunction(ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateChargebackKey(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.Count_chargebackContext ctx = (FraudoParser.Count_chargebackContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateRefundKey(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.Count_refundContext ctx = (FraudoParser.Count_refundContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

}
