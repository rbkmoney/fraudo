package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountKeyGenerator {

    public static String generate(ParserRuleContext context) {
        FraudoParser.CountContext ctx = (FraudoParser.CountContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by());
    }

    public static String generateSuccessKey(ParserRuleContext context) {
        FraudoParser.Count_successContext ctx = (FraudoParser.Count_successContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by());
    }

    public static String generateErrorKey(ParserRuleContext context) {
        FraudoParser.Count_errorContext ctx = (FraudoParser.Count_errorContext) context;
        return CommonKeyGenerator.generateKeyGroupedTwoFieldFunction(ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by());
    }

}
