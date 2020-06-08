package com.rbkmoney.fraudo.utils.key.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

import static com.rbkmoney.fraudo.FraudoPaymentParser.UniqueContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueKeyGenerator {

    public static <T> String generate(UniqueContext ctx, Function<String, T> resolve) {
        return CommonKeyGenerator.generateKeyGroupedTwoFieldFunction(
                ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

}
