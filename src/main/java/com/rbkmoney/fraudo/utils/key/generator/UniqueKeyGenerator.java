package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueKeyGenerator {
    public static String generate(ParserRuleContext context) {
        FraudoParser.UniqueContext ctx = (FraudoParser.UniqueContext) context;
        return CommonKeyGenerator.generateKeyGroupedTwoFieldFunction(
                ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by());
    }
}
