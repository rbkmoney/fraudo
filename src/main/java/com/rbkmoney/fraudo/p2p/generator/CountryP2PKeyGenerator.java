package com.rbkmoney.fraudo.p2p.generator;

import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryP2PKeyGenerator {

    public static String generate(FraudoP2PParser.Country_byContext ctx) {
        String fieldName = TextUtil.safeGetText(ctx.STRING());
        return new StringBuilder()
                .append(ctx.children.get(0))
                .append(fieldName)
                .toString();
    }

}
