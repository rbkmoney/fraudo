package com.rbkmoney.fraudo.utils.key.generator;

import com.rbkmoney.fraudo.FraudoPaymentParser;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryKeyGenerator {

    public static String generate(FraudoPaymentParser.Country_byContext ctx) {
        String fieldName = TextUtil.safeGetText(ctx.STRING());
        return new StringBuilder()
                .append(ctx.children.get(0))
                .append(fieldName)
                .toString();
    }

}
