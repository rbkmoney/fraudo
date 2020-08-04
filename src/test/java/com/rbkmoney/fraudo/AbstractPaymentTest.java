package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.FraudoPaymentParser.ParseContext;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.payment.aggregator.CountPaymentAggregator;
import com.rbkmoney.fraudo.payment.aggregator.SumPaymentAggregator;
import com.rbkmoney.fraudo.payment.factory.FraudVisitorFactoryImpl;
import com.rbkmoney.fraudo.payment.factory.FullVisitorFactoryImpl;
import com.rbkmoney.fraudo.payment.resolver.PaymentGroupResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTimeWindowResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTypeResolver;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.FieldResolver;
import com.rbkmoney.fraudo.test.constant.PaymentCheckedField;
import com.rbkmoney.fraudo.test.model.PaymentModel;
import com.rbkmoney.fraudo.test.payment.PaymentModelFieldResolver;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

public class AbstractPaymentTest {

    @Mock
    CountPaymentAggregator<PaymentModel, PaymentCheckedField> countPaymentAggregator;
    @Mock
    SumPaymentAggregator<PaymentModel, PaymentCheckedField> sumPaymentAggregator;
    @Mock
    UniqueValueAggregator<PaymentModel, PaymentCheckedField> uniqueValueAggregator;
    @Mock
    CountryResolver<PaymentCheckedField> countryResolver;
    @Mock
    InListFinder<PaymentModel, PaymentCheckedField> inListFinder;
    @Mock
    PaymentTimeWindowResolver timeWindowResolver;
    @Mock
    PaymentTypeResolver<PaymentModel> paymentModelPaymentTypeResolver;

    private FieldResolver<PaymentModel, PaymentCheckedField> fieldResolver = new PaymentModelFieldResolver();
    private PaymentGroupResolver<PaymentModel, PaymentCheckedField> paymentGroupResolver = new PaymentGroupResolver<>(fieldResolver);

    ResultModel parseAndVisit(InputStream resourceAsStream) throws IOException {
        ParseContext parse = getParseContext(resourceAsStream);
        return invokeParse(parse);
    }

    ResultModel invokeParse(ParseContext parse) {
        PaymentModel model = new PaymentModel();
        return invoke(parse, model);
    }

    ResultModel invoke(ParseContext parse, PaymentModel model) {
        return new FraudVisitorFactoryImpl()
                .createVisitor(
                        countPaymentAggregator,
                        sumPaymentAggregator,
                        uniqueValueAggregator,
                        countryResolver,
                        inListFinder,
                        fieldResolver,
                        paymentGroupResolver,
                        timeWindowResolver,
                        paymentModelPaymentTypeResolver)
                .visit(parse, model);
    }

    ResultModel invokeFullVisitor(ParseContext parse, PaymentModel model) {
        return new FullVisitorFactoryImpl()
                .createVisitor(
                        countPaymentAggregator,
                        sumPaymentAggregator,
                        uniqueValueAggregator,
                        countryResolver,
                        inListFinder,
                        fieldResolver,
                        paymentGroupResolver,
                        timeWindowResolver,
                        paymentModelPaymentTypeResolver)
                .visit(parse, model);
    }

    ParseContext getParseContext(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoPaymentLexer lexer = new com.rbkmoney.fraudo.FraudoPaymentLexer(new ANTLRInputStream(resourceAsStream));
        com.rbkmoney.fraudo.FraudoPaymentParser parser = new com.rbkmoney.fraudo.FraudoPaymentParser(new CommonTokenStream(lexer));
        return parser.parse();
    }

}
