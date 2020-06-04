package com.rbkmoney.fraudo.payment.factory;

import com.rbkmoney.fraudo.aggragator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.payment.aggregator.CountPaymentAggregator;
import com.rbkmoney.fraudo.payment.aggregator.SumPaymentAggregator;
import com.rbkmoney.fraudo.payment.resolver.PaymentGroupResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTimeWindowResolver;
import com.rbkmoney.fraudo.payment.visitor.impl.FirstFindVisitorImpl;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;

public interface FraudVisitorFactory {

    <T extends BaseModel, U> FirstFindVisitorImpl<T, U> createVisitor(CountPaymentAggregator<T, U> countPaymentAggregator,
                                                                      SumPaymentAggregator<T, U> sumPaymentAggregator,
                                                                      UniqueValueAggregator<T, U> uniqueValueAggregator,
                                                                      CountryResolver<U> countryResolver,
                                                                      InListFinder<T, U> listFinder,
                                                                      FieldResolver<T, U> fieldPairResolver,
                                                                      PaymentGroupResolver<T, U> paymentGroupResolver,
                                                                      PaymentTimeWindowResolver timeWindowResolver);

}
