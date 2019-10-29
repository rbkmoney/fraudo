package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.FieldPairResolver;
import com.rbkmoney.fraudo.resolver.payout.GroupByModelResolver;
import com.rbkmoney.fraudo.visitor.impl.FastFraudVisitorImpl;

public interface FraudVisitorFactory<T, U> {

    FastFraudVisitorImpl createVisitor(CountAggregator<T, U> countAggregator,
                                       SumAggregator<T, U> sumAggregator,
                                       UniqueValueAggregator<T, U> uniqueValueAggregator,
                                       CountryResolver<U> countryResolver,
                                       InListFinder<T, U> blackListFinder,
                                       InListFinder<T, U> whiteListFinder,
                                       InListFinder<T, U> greyListFinder,
                                       FieldNameResolver<U> fieldNameResolver,
                                       FieldPairResolver<T, U> fieldPairResolver,
                                       GroupByModelResolver<U> groupByModelResolver);

}
