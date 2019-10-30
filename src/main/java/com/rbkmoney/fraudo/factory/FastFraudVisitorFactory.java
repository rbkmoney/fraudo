package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.resolver.GroupByModelResolver;
import com.rbkmoney.fraudo.visitor.CountVisitor;
import com.rbkmoney.fraudo.visitor.CustomFuncVisitor;
import com.rbkmoney.fraudo.visitor.ListVisitor;
import com.rbkmoney.fraudo.visitor.SumVisitor;
import com.rbkmoney.fraudo.visitor.impl.*;

public class FastFraudVisitorFactory<T extends BaseModel, U> implements FraudVisitorFactory<T, U> {

    @Override
    public FastFraudVisitorImpl<T> createVisitor(
            CountAggregator<T, U> countAggregator,
            SumAggregator<T, U> sumAggregator,
            UniqueValueAggregator<T, U> uniqueValueAggregator,
            CountryResolver<U> countryResolver,
            InListFinder<T, U> listFinder,
            FieldResolver<T, U> fieldResolver,
            GroupByModelResolver<T, U> groupByModelResolver) {
        CountVisitor<T> countVisitor = new CountVisitorImpl<>(countAggregator, fieldResolver, groupByModelResolver);
        SumVisitor<T> sumVisitor = new SumVisitorImpl<>(sumAggregator, fieldResolver, groupByModelResolver);
        ListVisitor<T> listVisitor = new ListVisitorImpl<>(listFinder, fieldResolver);
        CustomFuncVisitor<T> customFuncVisitor = new CustomFuncVisitorImpl<>(
                uniqueValueAggregator,
                countryResolver,
                fieldResolver,
                groupByModelResolver);
        return new FastFraudVisitorImpl<>(countVisitor, sumVisitor, listVisitor, customFuncVisitor);
    }

}
