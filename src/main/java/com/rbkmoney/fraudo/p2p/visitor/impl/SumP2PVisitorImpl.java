package com.rbkmoney.fraudo.p2p.visitor.impl;

import com.rbkmoney.fraudo.p2p.aggragator.SumP2PAggregator;
import com.rbkmoney.fraudo.payment.aggregator.SumAggregator;
import com.rbkmoney.fraudo.p2p.resolver.P2PGroupResolver;
import com.rbkmoney.fraudo.p2p.visitor.SumP2PVisitor;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.resolver.TimeWindowResolver;
import com.rbkmoney.fraudo.utils.TextUtil;
import lombok.RequiredArgsConstructor;

import static com.rbkmoney.fraudo.FraudoP2PParser.SumContext;
import static com.rbkmoney.fraudo.FraudoP2PParser.Time_windowContext;

@RequiredArgsConstructor
public class SumP2PVisitorImpl<T, U> implements SumP2PVisitor<T> {

    private final SumP2PAggregator<T, U> sumAggregator;
    private final FieldResolver<T, U> fieldResolver;
    private final P2PGroupResolver<U> paymentGroupResolver;
    private final TimeWindowResolver<Time_windowContext> timeWindowResolver;

    @Override
    public Double visitSum(SumContext ctx, T model) {
        String countTarget = TextUtil.safeGetText(ctx.STRING());
        return sumAggregator.sum(
                fieldResolver.resolveName(countTarget),
                model,
                timeWindowResolver.resolve(ctx.time_window()),
                paymentGroupResolver.resolve(ctx.group_by())
        );
    }

}
