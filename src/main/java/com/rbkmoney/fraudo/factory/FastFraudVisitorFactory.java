package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.FraudoVisitor;
import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.visitor.*;

public class FastFraudVisitorFactory implements FraudVisitorFactory {

    @Override
    public FraudoVisitor<Object> createVisitor(FraudModel model, CountAggregator countAggregator,
                                               SumAggregator sumAggregator, UniqueValueAggregator uniqueValueAggregator,
                                               CountryResolver countryResolver, InListFinder blackListFinder,
                                               InListFinder whiteListFinder) {
        CountVisitorImpl countVisitor = new CountVisitorImpl(model, countAggregator);
        SumVisitorImpl sumVisitor = new SumVisitorImpl(model, sumAggregator);
        ListVisitorImpl listVisitor = new ListVisitorImpl(model, blackListFinder, whiteListFinder);
        CustomFuncVisitorImpl customFuncVisitor = new CustomFuncVisitorImpl(model, uniqueValueAggregator, countryResolver);
        return new FastFraudVisitorImpl(countVisitor, sumVisitor, listVisitor, customFuncVisitor);
    }
}
