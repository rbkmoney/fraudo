package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.factory.FastFraudVisitorFactory;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

public class AbstractFraudoTest {

    public static final String TEST_GMAIL_RU = "test@gmail.ru";

    @Mock
    CountAggregator countAggregator;
    @Mock
    SumAggregator sumAggregator;
    @Mock
    UniqueValueAggregator uniqueValueAggregator;
    @Mock
    CountryResolver countryResolver;
    @Mock
    InListFinder whiteListFinder;
    @Mock
    InListFinder blackListFinder;
    @Mock
    InListFinder greyListFinder;

    protected ResultModel parseAndVisit(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoParser.ParseContext parse = getParseContext(resourceAsStream);
        return invokeParse(parse);
    }

    protected ResultModel invokeParse(com.rbkmoney.fraudo.FraudoParser.ParseContext parse) {
        FraudModel model = new FraudModel();
        return invoke(parse, model);
    }

    protected ResultModel invoke(com.rbkmoney.fraudo.FraudoParser.ParseContext parse, FraudModel model) {
        return (ResultModel) new FastFraudVisitorFactory()
                .createVisitor(model, countAggregator, sumAggregator, uniqueValueAggregator, countryResolver,
                        blackListFinder, whiteListFinder, greyListFinder)
                .visit(parse);
    }

    protected com.rbkmoney.fraudo.FraudoParser.ParseContext getParseContext(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoLexer lexer = new com.rbkmoney.fraudo.FraudoLexer(new ANTLRInputStream(resourceAsStream));
        com.rbkmoney.fraudo.FraudoParser parser = new com.rbkmoney.fraudo.FraudoParser(new CommonTokenStream(lexer));

        return parser.parse();
    }

}
