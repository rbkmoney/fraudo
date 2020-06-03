package com.rbkmoney.fraudo.payment.factory;

import com.rbkmoney.fraudo.aggragator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.payment.aggregator.CountAggregator;
import com.rbkmoney.fraudo.payment.aggregator.SumAggregator;
import com.rbkmoney.fraudo.payment.resolver.PaymentGroupResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTimeWindowResolver;
import com.rbkmoney.fraudo.payment.visitor.impl.FirstFindVisitorImpl;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;

public interface FraudVisitorFactory {

    <T extends BaseModel, U> FirstFindVisitorImpl createVisitor(CountAggregator<T, U> countAggregator,
                                                                SumAggregator<T, U> sumAggregator,
                                                                UniqueValueAggregator<T, U> uniqueValueAggregator,
                                                                CountryResolver<U> countryResolver,
                                                                InListFinder<T, U> listFinder,
                                                                FieldResolver<T, U> fieldPairResolver,
                                                                PaymentGroupResolver<T, U> paymentGroupResolver,
                                                                PaymentTimeWindowResolver timeWindowResolver);

}
