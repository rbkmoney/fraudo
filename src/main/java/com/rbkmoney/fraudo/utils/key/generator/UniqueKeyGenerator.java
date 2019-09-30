package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueKeyGenerator {
    public static String generate(ParserRuleContext context) {
        FraudoParser.UniqueContext ctx = (FraudoParser.UniqueContext) context;
        String field = TextUtil.safeGetText(ctx.STRING(0));
        String fieldBy = TextUtil.safeGetText(ctx.STRING(1));
        return new StringBuilder()
                .append(ctx.children.get(0))
                .append(field)
                .append(fieldBy)
                .toString();
    }
}
