package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueKeyGenerator {

    public static <T> String generate(ParserRuleContext context, Function<String, T> resolve) {
        FraudoParser.UniqueContext ctx = (FraudoParser.UniqueContext) context;
        return CommonKeyGenerator.generateKeyGroupedTwoFieldFunction(
                ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

}
