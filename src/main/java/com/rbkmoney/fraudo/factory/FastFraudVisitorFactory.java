package com.rbkmoney.fraudo.factory;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.PaymentModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldNameResolver;
import com.rbkmoney.fraudo.resolver.FieldPairResolver;
import com.rbkmoney.fraudo.resolver.payout.GroupByModelResolver;
import com.rbkmoney.fraudo.visitor.*;

public class FastFraudVisitorFactory implements FraudVisitorFactory<PaymentModel, CheckedField> {

    @Override
    public FastFraudVisitorImpl<PaymentModel> createVisitor(
            CountAggregator<PaymentModel, CheckedField> countAggregator,
            SumAggregator<PaymentModel, CheckedField> sumAggregator,
            UniqueValueAggregator<PaymentModel, CheckedField> uniqueValueAggregator,
            CountryResolver<CheckedField> countryResolver,
            InListFinder<PaymentModel, CheckedField> blackListFinder,
            InListFinder<PaymentModel, CheckedField> whiteListFinder,
            InListFinder<PaymentModel, CheckedField> greyListFinder,
            FieldNameResolver<CheckedField> fieldNameResolver,
            FieldPairResolver<PaymentModel, CheckedField> fieldPairResolver,
            GroupByModelResolver<CheckedField> groupByModelResolver) {
        CountVisitor<PaymentModel> countVisitor = new CountVisitorImpl<>(countAggregator, fieldNameResolver, groupByModelResolver);
        SumVisitor<PaymentModel> sumVisitor = new SumVisitorImpl<>(sumAggregator, fieldNameResolver, groupByModelResolver);
        ListVisitor<PaymentModel> listVisitor = new ListVisitorImpl<>(blackListFinder, whiteListFinder, greyListFinder, fieldPairResolver);
        CustomFuncVisitor<PaymentModel> customFuncVisitor = new CustomFuncVisitorImpl<>(
                uniqueValueAggregator,
                countryResolver,
                fieldPairResolver,
                fieldNameResolver,
                groupByModelResolver);
        return new FastFraudVisitorImpl<>(countVisitor, sumVisitor, listVisitor, customFuncVisitor);
    }
}
