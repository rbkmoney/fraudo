package com.rbkmoney.fraudo.p2p.factory;

import com.rbkmoney.fraudo.aggragator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.p2p.aggragator.CountP2PAggregator;
import com.rbkmoney.fraudo.p2p.aggragator.SumP2PAggregator;
import com.rbkmoney.fraudo.p2p.resolver.P2PGroupResolver;
import com.rbkmoney.fraudo.p2p.resolver.P2PTimeWindowResolver;
import com.rbkmoney.fraudo.p2p.visitor.CountP2PVisitor;
import com.rbkmoney.fraudo.p2p.visitor.CustomP2PFuncVisitor;
import com.rbkmoney.fraudo.p2p.visitor.ListP2PVisitor;
import com.rbkmoney.fraudo.p2p.visitor.SumP2PVisitor;
import com.rbkmoney.fraudo.p2p.visitor.impl.*;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;

public class P2PFraudVisitorFactory implements FraudP2PVisitorFactory {

    @Override
    public <T extends BaseModel, U> FirstFindP2PVisitorImpl<T, U> createVisitor(
            CountP2PAggregator<T, U> countAggregator,
            SumP2PAggregator<T, U> sumAggregator,
            UniqueValueAggregator<T, U> uniqueValueAggregator,
            CountryResolver<U> countryResolver,
            InListFinder<T, U> listFinder,
            FieldResolver<T, U> fieldResolver,
            P2PGroupResolver<U> groupResolver,
            P2PTimeWindowResolver timeWindowResolver) {
        CountP2PVisitor<T> countVisitor = new CountP2PVisitorImpl<>(countAggregator, fieldResolver, groupResolver, timeWindowResolver);
        SumP2PVisitor<T> sumVisitor = new SumP2PVisitorImpl<>(sumAggregator, fieldResolver, groupResolver, timeWindowResolver);
        ListP2PVisitor<T> listVisitor = new ListP2PVisitorImpl<>(listFinder, fieldResolver);
        CustomP2PFuncVisitor<T> customFuncVisitor = new CustomP2PFuncVisitorImpl<>(uniqueValueAggregator, countryResolver,
                fieldResolver, groupResolver, timeWindowResolver);
        return new FirstFindP2PVisitorImpl<>(countVisitor, sumVisitor, listVisitor, customFuncVisitor, fieldResolver);
    }

}
