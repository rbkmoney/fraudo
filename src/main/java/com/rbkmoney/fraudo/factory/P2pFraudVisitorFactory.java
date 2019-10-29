package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.constant.P2PCheckedField;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.P2PModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.FieldPairResolver;
import com.rbkmoney.fraudo.resolver.payout.GroupByModelResolver;
import com.rbkmoney.fraudo.visitor.*;
import com.rbkmoney.fraudo.visitor.impl.*;

public class P2pFraudVisitorFactory implements FraudVisitorFactory<P2PModel, P2PCheckedField> {

    @Override
    public FastFraudVisitorImpl<P2PModel> createVisitor(
            CountAggregator<P2PModel, P2PCheckedField> countAggregator,
            SumAggregator<P2PModel, P2PCheckedField> sumAggregator,
            UniqueValueAggregator<P2PModel, P2PCheckedField> uniqueValueAggregator,
            CountryResolver<P2PCheckedField> countryResolver,
            InListFinder<P2PModel, P2PCheckedField> blackListFinder,
            InListFinder<P2PModel, P2PCheckedField> whiteListFinder,
            InListFinder<P2PModel, P2PCheckedField> greyListFinder,
            FieldNameResolver<P2PCheckedField> fieldNameResolver,
            FieldPairResolver<P2PModel, P2PCheckedField> fieldPairResolver,
            GroupByModelResolver<P2PCheckedField> groupByModelResolver) {
        CountVisitor<P2PModel> countVisitor = new CountVisitorImpl<>(countAggregator, fieldNameResolver, groupByModelResolver);
        SumVisitor<P2PModel> sumVisitor = new SumVisitorImpl<>(sumAggregator, fieldNameResolver, groupByModelResolver);
        ListVisitor<P2PModel> listVisitor = new ListVisitorImpl<>(blackListFinder, whiteListFinder, greyListFinder, fieldPairResolver);
        CustomFuncVisitor<P2PModel> customFuncVisitor = new CustomFuncVisitorImpl<>(
                uniqueValueAggregator,
                countryResolver,
                fieldPairResolver,
                fieldNameResolver,
                groupByModelResolver);
        return new FastFraudVisitorImpl<>(countVisitor, sumVisitor, listVisitor, customFuncVisitor);
    }
}
