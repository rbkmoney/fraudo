package com.rbkmoney.fraudo.p2p;

import com.rbkmoney.fraudo.aggragator.CountAggregator;
import com.rbkmoney.fraudo.aggragator.SumAggregator;
import com.rbkmoney.fraudo.aggragator.UniqueValueAggregator;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.p2p.factory.P2PFraudVisitorFactory;
import com.rbkmoney.fraudo.p2p.resolver.P2PGroupResolver;
import com.rbkmoney.fraudo.p2p.resolver.P2PTimeWindowResolver;
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
    P2PTimeWindowResolver timeWindowResolver;

    private P2PModelFieldResolver fieldResolver = new P2PModelFieldResolver();
    private P2PGroupResolver<P2PModel, P2PCheckedField> paymentGroupResolver = new P2PGroupResolver<>(fieldResolver);


    ResultModel parseAndVisit(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoP2PParser.ParseContext parse = getParseContext(resourceAsStream);
        return invokeParse(parse);
    }

    ResultModel invokeParse(com.rbkmoney.fraudo.FraudoP2PParser.ParseContext parse) {
        P2PModel model = new P2PModel();
        return invoke(parse, model);
    }

    ResultModel invoke(com.rbkmoney.fraudo.FraudoP2PParser.ParseContext parse, P2PModel model) {
        return new P2PFraudVisitorFactory()
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

    com.rbkmoney.fraudo.FraudoP2PParser.ParseContext getParseContext(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoP2PLexer lexer = new com.rbkmoney.fraudo.FraudoP2PLexer(new ANTLRInputStream(resourceAsStream));
        com.rbkmoney.fraudo.FraudoP2PParser parser = new com.rbkmoney.fraudo.FraudoP2PParser(new CommonTokenStream(lexer));

        return parser.parse();
    }

}
