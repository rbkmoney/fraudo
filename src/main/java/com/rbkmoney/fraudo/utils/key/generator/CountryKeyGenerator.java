package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoParser;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryKeyGenerator {

    public static String generate(ParserRuleContext context) {
        FraudoParser.Country_byContext ctx = (FraudoParser.Country_byContext) context;
        String fieldName = TextUtil.safeGetText(ctx.STRING());
        return new StringBuilder()
                .append(ctx.children.get(0))
                .append(fieldName)
                .toString();
    }

}
