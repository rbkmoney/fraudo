package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SumKeyGenerator {

    public static String generate(ParserRuleContext context) {
        FraudoParser.SumContext ctx = (FraudoParser.SumContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by());
    }

    public static String generateSuccessKey(ParserRuleContext context) {
        FraudoParser.Sum_successContext ctx = (FraudoParser.Sum_successContext) context;
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by());
    }

    public static String generateErrorKey(ParserRuleContext context) {
        FraudoParser.Sum_errorContext ctx = (FraudoParser.Sum_errorContext) context;
        return CommonKeyGenerator.generateKeyGroupedErrorFunction(ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by());
    }

}
