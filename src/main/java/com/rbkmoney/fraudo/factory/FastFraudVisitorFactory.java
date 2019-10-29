package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.finder.InNamingListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.FieldPairResolver;
import com.rbkmoney.fraudo.resolver.payout.GroupByModelResolver;
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
            InListFinder<T, U> blackListFinder,
            InListFinder<T, U> whiteListFinder,
            InListFinder<T, U> greyListFinder,
            InNamingListFinder<T, U> inNamingListFinder,
            FieldNameResolver<U> fieldNameResolver,
            FieldPairResolver<T, U> fieldPairResolver,
            GroupByModelResolver<U> groupByModelResolver) {
        CountVisitor<T> countVisitor = new CountVisitorImpl<>(countAggregator, fieldNameResolver, groupByModelResolver);
        SumVisitor<T> sumVisitor = new SumVisitorImpl<>(sumAggregator, fieldNameResolver, groupByModelResolver);
        ListVisitor<T> listVisitor = new ListVisitorImpl<>(blackListFinder, whiteListFinder, greyListFinder, inNamingListFinder, fieldPairResolver);
        CustomFuncVisitor<T> customFuncVisitor = new CustomFuncVisitorImpl<>(
                uniqueValueAggregator,
                countryResolver,
                fieldPairResolver,
                fieldNameResolver,
                groupByModelResolver);
        return new FastFraudVisitorImpl<>(countVisitor, sumVisitor, listVisitor, customFuncVisitor);
    }

}
