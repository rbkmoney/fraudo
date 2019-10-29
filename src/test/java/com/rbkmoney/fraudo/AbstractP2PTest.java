package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.constant.P2PCheckedField;
import com.rbkmoney.fraudo.factory.FastFraudVisitorFactory;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.P2PModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import com.rbkmoney.fraudo.resolver.p2p.P2PModelFieldNameResolver;
import com.rbkmoney.fraudo.resolver.p2p.P2PModelFieldValueResolver;
import com.rbkmoney.fraudo.resolver.payout.GroupByModelResolver;
import com.rbkmoney.fraudo.resolver.payout.PaymentModelFieldPairResolver;
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
    InListFinder<P2PModel, P2PCheckedField> whiteListFinder;
    @Mock
    InListFinder<P2PModel, P2PCheckedField> blackListFinder;
    @Mock
    InListFinder<P2PModel, P2PCheckedField> greyListFinder;

    private P2PModelFieldNameResolver paymentModelFieldNameResolver = new P2PModelFieldNameResolver();
    private P2PModelFieldValueResolver p2PModelFieldValueResolver = new P2PModelFieldValueResolver();
    private GroupByModelResolver<P2PCheckedField> groupByModelResolver = new GroupByModelResolver<>(paymentModelFieldNameResolver);
    private PaymentModelFieldPairResolver<P2PModel, P2PCheckedField> paymentModelFieldPairResolver = new PaymentModelFieldPairResolver<>(
            paymentModelFieldNameResolver,
            p2PModelFieldValueResolver);

    ResultModel parseAndVisit(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoParser.ParseContext parse = getParseContext(resourceAsStream);
        return invokeParse(parse);
    }

    ResultModel invokeParse(com.rbkmoney.fraudo.FraudoParser.ParseContext parse) {
        P2PModel model = new P2PModel();
        return invoke(parse, model);
    }

    ResultModel invoke(com.rbkmoney.fraudo.FraudoParser.ParseContext parse, P2PModel model) {
        return (ResultModel) new FastFraudVisitorFactory<P2PModel, P2PCheckedField>()
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
