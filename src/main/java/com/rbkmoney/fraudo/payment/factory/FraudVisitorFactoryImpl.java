package com.rbkmoney.fraudo.payment.factory;

import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.converter.TrustConditionConverter;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.BaseModel;
import com.rbkmoney.fraudo.payment.aggregator.CountPaymentAggregator;
import com.rbkmoney.fraudo.payment.aggregator.SumPaymentAggregator;
import com.rbkmoney.fraudo.payment.resolver.CustomerTypeResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentGroupResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTimeWindowResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTypeResolver;
import com.rbkmoney.fraudo.payment.visitor.CountVisitor;
import com.rbkmoney.fraudo.payment.visitor.CustomFuncVisitor;
import com.rbkmoney.fraudo.payment.visitor.IsTrustedFuncVisitor;
import com.rbkmoney.fraudo.payment.visitor.ListVisitor;
import com.rbkmoney.fraudo.payment.visitor.SumVisitor;
import com.rbkmoney.fraudo.payment.visitor.impl.*;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;

public class FraudVisitorFactoryImpl implements FraudVisitorFactory {

    @Override
    public <T extends BaseModel, U> FirstFindVisitorImpl<T, U> createVisitor(
            CountPaymentAggregator<T, U> countPaymentAggregator,
            SumPaymentAggregator<T, U> sumPaymentAggregator,
            UniqueValueAggregator<T, U> uniqueValueAggregator,
            CountryResolver<U> countryResolver,
            InListFinder<T, U> listFinder,
            FieldResolver<T, U> fieldResolver,
            PaymentGroupResolver<T, U> paymentGroupResolver,
            PaymentTimeWindowResolver timeWindowResolver,
            PaymentTypeResolver<T> paymentTypeResolver,
            CustomerTypeResolver<T> customerTypeResolver) {
        CountVisitor<T> countVisitor =
                new CountVisitorImpl<>(countPaymentAggregator, fieldResolver, paymentGroupResolver, timeWindowResolver);
        SumVisitor<T> sumVisitor =
                new SumVisitorImpl<>(sumPaymentAggregator, fieldResolver, paymentGroupResolver, timeWindowResolver);
        ListVisitor<T> listVisitor = new ListVisitorImpl<>(listFinder, fieldResolver);
        CustomFuncVisitor<T> customFuncVisitor = new CustomFuncVisitorImpl<>(
                uniqueValueAggregator,
                countryResolver,
                fieldResolver,
                paymentGroupResolver,
                timeWindowResolver,
                paymentTypeResolver
        );
        IsTrustedFuncVisitor<T> isTrustedFuncVisitor = new IsTrustedFuncVisitorImpl<>(customerTypeResolver);
        TrustConditionConverter trustConditionConverter = new TrustConditionConverter();

        return new FirstFindVisitorImpl<>(
                countVisitor,
                sumVisitor,
                listVisitor,
                customFuncVisitor,
                isTrustedFuncVisitor,
                fieldResolver,
                trustConditionConverter
        );
    }

}
