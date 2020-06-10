package com.rbkmoney.fraudo.utils.key.generator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

import static com.rbkmoney.fraudo.FraudoPaymentParser.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SumKeyGenerator {

    public static <T> String generate(SumContext ctx, Function<String, T> resolve) {
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateSuccessKey(Sum_successContext ctx, Function<String, T> resolve) {
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateErrorKey(Sum_errorContext ctx, Function<String, T> resolve) {
        return CommonKeyGenerator.generateKeyGroupedTwoFieldFunction(ctx.STRING(0),
                ctx.STRING(1),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateChargebackKey(Sum_chargebackContext ctx, Function<String, T> resolve) {
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

    public static <T> String generateRefundKey(Sum_refundContext ctx, Function<String, T> resolve) {
        return CommonKeyGenerator.generateKeyGroupedFunction(ctx.STRING(),
                ctx.children.get(0),
                ctx.time_window(),
                ctx.group_by(),
                resolve);
    }

}
