package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.FraudoVisitor;
import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;

public interface FraudVisitorFactory {

    FraudoVisitor<Object> createVisitor(FraudModel model, CountAggregator countAggregator,
                                        SumAggregator sumAggregator, UniqueValueAggregator uniqueValueAggregator,
                                        CountryResolver countryResolver, InListFinder blackListFinder,
                                        InListFinder whiteListFinder);

}
