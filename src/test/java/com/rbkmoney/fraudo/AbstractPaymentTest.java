package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.factory.FastFraudVisitorFactory;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.PaymentModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.payout.GroupByModelResolver;
import com.rbkmoney.fraudo.resolver.payout.PaymentModelFieldNameResolver;
import com.rbkmoney.fraudo.resolver.payout.PaymentModelFieldPairResolver;
import com.rbkmoney.fraudo.resolver.payout.PaymentModelFieldValueResolver;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

public class AbstractPaymentTest {

    @Mock
    CountAggregator<PaymentModel, CheckedField> countAggregator;
    @Mock
    SumAggregator<PaymentModel, CheckedField> sumAggregator;
    @Mock
    UniqueValueAggregator<PaymentModel, CheckedField> uniqueValueAggregator;
    @Mock
    CountryResolver<CheckedField> countryResolver;
    @Mock
    InListFinder<PaymentModel, CheckedField> whiteListFinder;
    @Mock
    InListFinder<PaymentModel, CheckedField> blackListFinder;
    @Mock
    InListFinder<PaymentModel, CheckedField> greyListFinder;

    private PaymentModelFieldNameResolver paymentModelFieldNameResolver = new PaymentModelFieldNameResolver();
    private PaymentModelFieldValueResolver payoutModelFieldValueResolver = new PaymentModelFieldValueResolver();
    private GroupByModelResolver<CheckedField> groupByModelResolver = new GroupByModelResolver<CheckedField>(paymentModelFieldNameResolver);
    private PaymentModelFieldPairResolver paymentModelFieldPairResolver = new PaymentModelFieldPairResolver(
            paymentModelFieldNameResolver,
            payoutModelFieldValueResolver);

    ResultModel parseAndVisit(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoParser.ParseContext parse = getParseContext(resourceAsStream);
        return invokeParse(parse);
    }

    ResultModel invokeParse(com.rbkmoney.fraudo.FraudoParser.ParseContext parse) {
        PaymentModel model = new PaymentModel();
        return invoke(parse, model);
    }

    ResultModel invoke(com.rbkmoney.fraudo.FraudoParser.ParseContext parse, PaymentModel model) {
        return (ResultModel) new FastFraudVisitorFactory()
                .createVisitor(
                        countAggregator,
                        sumAggregator,
                        uniqueValueAggregator,
                        countryResolver,
                        blackListFinder,
                        whiteListFinder,
                        greyListFinder,
                        paymentModelFieldNameResolver,
                        paymentModelFieldPairResolver,
                        groupByModelResolver)
                .visit(parse, model);
    }

    com.rbkmoney.fraudo.FraudoParser.ParseContext getParseContext(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoLexer lexer = new com.rbkmoney.fraudo.FraudoLexer(new ANTLRInputStream(resourceAsStream));
        com.rbkmoney.fraudo.FraudoParser parser = new com.rbkmoney.fraudo.FraudoParser(new CommonTokenStream(lexer));

        return parser.parse();
    }

}
