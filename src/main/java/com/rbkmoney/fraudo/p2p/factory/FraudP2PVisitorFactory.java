package com.rbkmoney.fraudo.p2p.factory;

import com.rbkmoney.fraudo.aggragator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.p2p.aggragator.CountP2PAggregator;
import com.rbkmoney.fraudo.p2p.aggragator.SumP2PAggregator;
import com.rbkmoney.fraudo.p2p.resolver.P2PGroupResolver;
import com.rbkmoney.fraudo.p2p.resolver.P2PTimeWindowResolver;
import com.rbkmoney.fraudo.p2p.visitor.impl.FirstFindP2PVisitorImpl;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;

public interface FraudP2PVisitorFactory {

    <T extends BaseModel, U> FirstFindP2PVisitorImpl createVisitor(CountP2PAggregator<T, U> countAggregator,
                                                                   SumP2PAggregator<T, U> sumAggregator,
                                                                   UniqueValueAggregator<T, U> uniqueValueAggregator,
                                                                   CountryResolver<U> countryResolver,
                                                                   InListFinder<T, U> listFinder,
                                                                   FieldResolver<T, U> fieldPairResolver,
                                                                   P2PGroupResolver<T, U> paymentGroupResolver,
                                                                   P2PTimeWindowResolver timeWindowResolver);

}
