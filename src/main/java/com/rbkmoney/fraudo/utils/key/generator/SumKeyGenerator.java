package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SumKeyGenerator {

    public static <T> String generate(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.SumContext ctx = (FraudoParser.SumContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateSuccessKey(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.Sum_successContext ctx = (FraudoParser.Sum_successContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateErrorKey(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.Sum_errorContext ctx = (FraudoParser.Sum_errorContext) context;
        return CommonKeyGenerator.generateKeyGroupedTwoFieldFunction(ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

}
