package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.aggregator.CountAggregator;
import com.rbkmoney.fraudo.aggregator.SumAggregator;
import com.rbkmoney.fraudo.aggregator.UniqueValueAggregator;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.factory.FastFraudVisitorFactory;
import com.rbkmoney.fraudo.finder.InListFinder;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.resolver.CountryResolver;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Matchers.*;

public class FraudoTest {

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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void threeDsTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/three_ds.frd");
        com.rbkmoney.fraudo.FraudoLexer lexer = new com.rbkmoney.fraudo.FraudoLexer(new ANTLRInputStream(resourceAsStream));
        com.rbkmoney.fraudo.FraudoParser parser = new com.rbkmoney.fraudo.FraudoParser(new CommonTokenStream(lexer));

        Mockito.when(countAggregator.count(anyObject(), anyString(), anyLong())).thenReturn(10);
        Object result = new FastFraudVisitorFactory().createVisitor(new FraudModel(), countAggregator,
                sumAggregator, uniqueValueAggregator, countryResolver, blackListFinder, whiteListFinder).visit(parser.parse());
        Assert.assertEquals(ResultStatus.THREE_DS, result);
    }

    @Test
    public void notifyTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/notify.frd");
        Object result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.NOTIFY, result);
    }

    @Test
    public void declineTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/decline.frd");
        Object result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.DECLINE, result);
    }

    @Test
    public void acceptTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/accept.frd");
        Object result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.ACCEPT, result);
    }

    @Test
    public void ruleIsNotFireTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/rule_is_not_fire.frd");
        Object result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.ACCEPT, result);
    }

    @Test(expected = UnknownResultException.class)
    public void notImplOperatorTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/unknownResult.frd");
        parseAndVisit(resourceAsStream);
    }

    @Test
    public void countTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/count.frd");
        Mockito.when(countAggregator.count(anyObject(), anyString(), anyLong())).thenReturn(10);
        Mockito.when(countAggregator.countError(anyObject(), anyString(), anyLong(), anyString())).thenReturn(6);
        Mockito.when(countAggregator.countSuccess(anyObject(), anyString(), anyLong())).thenReturn(4);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        Object result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NOTIFY, result);

        Mockito.when(countAggregator.count(anyObject(), anyString(), anyLong())).thenReturn(9);
        Mockito.when(countAggregator.countError(anyObject(), anyString(), anyLong(), anyString())).thenReturn(6);
        Mockito.when(countAggregator.countSuccess(anyObject(), anyString(), anyLong())).thenReturn(6);

        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NOTIFY, result);
    }

    @Test
    public void sumTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/sum.frd");
        Mockito.when(sumAggregator.sum(anyObject(), anyString(), anyLong())).thenReturn(10500.60);
        Mockito.when(sumAggregator.sumError(anyObject(), anyString(), anyLong(), anyString())).thenReturn(524.0);
        Mockito.when(sumAggregator.sumSuccess(anyObject(), anyString(), anyLong())).thenReturn(4.0);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        Object result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NOTIFY, result);

        Mockito.when(sumAggregator.sum(anyObject(), anyString(), anyLong())).thenReturn(90.0);
        Mockito.when(sumAggregator.sumError(anyObject(), anyString(), anyLong(), anyString())).thenReturn(524.0);
        Mockito.when(sumAggregator.sumSuccess(anyObject(), anyString(), anyLong())).thenReturn(501.0);

        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NOTIFY, result);
    }

    @Test
    public void inTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/in.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        model.setEmail(TEST_GMAIL_RU);
        Object result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.NOTIFY, result);
    }

    @Test
    public void likeTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/like.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        model.setEmail(TEST_GMAIL_RU);
        Object result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.NOTIFY, result);

        model.setEmail("teeeee");
        result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.ACCEPT, result);
    }

    @Test
    public void inNotTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/in_not.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        model.setEmail(TEST_GMAIL_RU);
        Object result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.ACCEPT, result);
    }

    @Test
    public void uniqCountTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/count_uniq.frd");
        Mockito.when(uniqueValueAggregator.countUniqueValue(any(), any())).thenReturn(2);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        Object result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result);
    }

    @Test
    public void whiteBlackListTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/whitelist.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(whiteListFinder.findInList(any(), anyString())).thenReturn(true);
        Object result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NOTIFY, result);

        resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/blacklist.frd");
        parseContext = getParseContext(resourceAsStream);
        Mockito.when(blackListFinder.findInList(any(), anyString())).thenReturn(true);
        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NOTIFY, result);
    }

    @Test
    public void eqCountryTest() throws Exception {
        InputStream resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/eq_country.frd");

        Mockito.when(countryResolver.resolveCountryBank(anyString())).thenReturn("RU");
        Mockito.when(countryResolver.resolveCountryIp(anyString())).thenReturn("RU");

        Object result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.NOTIFY, result);

        Mockito.when(countryResolver.resolveCountryBank(anyString())).thenReturn("US");
        resourceAsStream = FraudoTest.class.getResourceAsStream("/rules/eq_country.frd");
        result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.ACCEPT, result);
    }

    private Object parseAndVisit(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoParser.ParseContext parse = getParseContext(resourceAsStream);
        return invokeParse(parse);
    }

    private Object invokeParse(com.rbkmoney.fraudo.FraudoParser.ParseContext parse) {
        FraudModel model = new FraudModel();
        return invoke(parse, model);
    }

    private Object invoke(com.rbkmoney.fraudo.FraudoParser.ParseContext parse, FraudModel model) {
        return new FastFraudVisitorFactory().createVisitor(model, countAggregator,
                sumAggregator, uniqueValueAggregator, countryResolver, blackListFinder, whiteListFinder).visit(parse);
    }

    private com.rbkmoney.fraudo.FraudoParser.ParseContext getParseContext(InputStream resourceAsStream) throws IOException {
        com.rbkmoney.fraudo.FraudoLexer lexer = new com.rbkmoney.fraudo.FraudoLexer(new ANTLRInputStream(resourceAsStream));
        com.rbkmoney.fraudo.FraudoParser parser = new com.rbkmoney.fraudo.FraudoParser(new CommonTokenStream(lexer));

        return parser.parse();
    }
}
