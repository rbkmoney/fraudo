package com.rbkmoney.fraudo.p2p.generator;

import com.rbkmoney.fraudo.FraudoP2PParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountP2PKeyGenerator {

    public static <T> String generate(FraudoP2PParser.CountContext ctx, Function<String, T> resolve) {
        return CommonP2PKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

}
