package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.resolver.GroupByModelResolver;
import com.rbkmoney.fraudo.visitor.impl.FirstFindVisitorImpl;

public interface FraudVisitorFactory {

    <T extends BaseModel, U> FirstFindVisitorImpl createVisitor(CountAggregator<T, U> countAggregator,
                                                                SumAggregator<T, U> sumAggregator,
                                                                UniqueValueAggregator<T, U> uniqueValueAggregator,
                                                                CountryResolver<U> countryResolver,
                                                                InListFinder<T, U> listFinder,
                                                                FieldResolver<T, U> fieldPairResolver,
                                                                GroupByModelResolver<T, U> groupByModelResolver);

}
