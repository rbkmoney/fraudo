package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.payment.aggregator.CountAggregator;
import com.rbkmoney.fraudo.payment.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggragator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.payment.factory.FraudVisitorFactoryImpl;
import com.rbkmoney.fraudo.payment.resolver.PaymentGroupResolver;
import com.rbkmoney.fraudo.payment.resolver.PaymentTimeWindowResolver;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.test.constant.P2PCheckedField;
import com.rbkmoney.fraudo.test.model.P2PModel;
import com.rbkmoney.fraudo.test.p2p.P2PModelFieldResolver;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

public class AbstractP2PTest {

    @Mock
    CountAggregator<P2PModel, P2PCheckedField> countAggregator;
    @Mock
    SumAggregator<P2PModel, P2PCheckedField> sumAggregator;
    @Mock
    UniqueValueAggregator<P2PModel, P2PCheckedField> uniqueValueAggregator;
    @Mock
    CountryResolver<P2PCheckedField> countryResolver;
    @Mock
    InListFinder<P2PModel, P2PCheckedField> listFinder;
    @Mock
    PaymentTimeWindowResolver timeWindowResolver;

    private P2PModelFieldResolver fieldResolver = new P2PModelFieldResolver();
    private PaymentGroupResolver<P2PModel, P2PCheckedField> paymentGroupResolver = new PaymentGroupResolver<>(fieldResolver);


    ResultModel parseAndVisit(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoParser.ParseContext parse = getParseContext(resourceAsStream);
        return invokeParse(parse);
    }

    ResultModel invokeParse(com.rbkmoney.fraudo.FraudoParser.ParseContext parse) {
        P2PModel model = new P2PModel();
        return invoke(parse, model);
    }

    ResultModel invoke(com.rbkmoney.fraudo.FraudoParser.ParseContext parse, P2PModel model) {
        return (ResultModel) new FraudVisitorFactoryImpl()
                .createVisitor(
                        countAggregator,
                        sumAggregator,
                        uniqueValueAggregator,
                        countryResolver,
                        listFinder,
                        fieldResolver,
                        paymentGroupResolver,
                        timeWindowResolver)
                .visit(parse, model);
    }

    com.rbkmoney.fraudo.FraudoParser.ParseContext getParseContext(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoLexer lexer = new com.rbkmoney.fraudo.FraudoLexer(new ANTLRInputStream(resourceAsStream));
        com.rbkmoney.fraudo.FraudoParser parser = new com.rbkmoney.fraudo.FraudoParser(new CommonTokenStream(lexer));

        return parser.parse();
    }

}
