package com.rbkmoney.fraudo.p2p.generator;

import com.rbkmoney.fraudo.FraudoP2PParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueP2PKeyGenerator {

    public static <T> String generate(FraudoP2PParser.UniqueContext ctx, Function<String, T> resolve) {
        return CommonP2PKeyGenerator.generateKeyGroupedTwoFieldFunction(
                ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

}
