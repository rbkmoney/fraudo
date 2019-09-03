package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.mockito.Matchers.*;

public class CustomTest extends AbstractFraudoTest {

    public static final String TEST_GMAIL_RU = "test@gmail.ru";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void threeDsTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/three_ds.frd");
        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ResultModel result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.THREE_DS, result.getResultStatus());
    }

    @Test
    public void notifyTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/notify.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(1, result.getNotificationsRule().size());
    }

    @Test
    public void declineTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/decline.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        Assert.assertEquals("test_11", result.getRuleChecked());
    }

    @Test
    public void acceptTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/accept.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
    }

    @Test
    public void ruleIsNotFireTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/rule_is_not_fire.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
    }

    @Test(expected = UnknownResultException.class)
    public void notImplOperatorTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/unknownResult.frd");
        parseAndVisit(resourceAsStream);
    }

    @Test
    public void inTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/in.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        model.setEmail(TEST_GMAIL_RU);
        ResultModel result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
    }

    @Test
    public void inCountryTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/in_country.frd");
        Mockito.when(countryResolver.resolveCountry(any(), anyString())).thenReturn("RU");

        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        ResultModel result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
    }

    @Test
    public void amountTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/amount.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        model.setAmount(56L);
        ResultModel result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
    }

    @Test
    public void catchTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/catch.frd");
        Mockito.when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any())).thenThrow(new UnknownResultException("as"));
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
    }

    @Test
    public void likeTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/like.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        model.setEmail(TEST_GMAIL_RU);
        ResultModel result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());

        model.setEmail("teeeee");
        result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
    }

    @Test
    public void inNotTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/in_not.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        FraudModel model = new FraudModel();
        model.setEmail(TEST_GMAIL_RU);
        ResultModel result = invoke(parseContext, model);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
    }

    @Test
    public void uniqCountTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/count_uniq.frd");
        Mockito.when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any())).thenReturn(2);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
    }

    @Test
    public void eqCountryTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/eq_country.frd");

        Mockito.when(countryResolver.resolveCountry(any(), anyString())).thenReturn("RU");

        ResultModel result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(1, result.getNotificationsRule().size());

        Mockito.when(countryResolver.resolveCountry(any(), anyString())).thenReturn("US");
        resourceAsStream = CustomTest.class.getResourceAsStream("/rules/eq_country.frd");
        result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());

        resourceAsStream = CustomTest.class.getResourceAsStream("/rules/accept_with_notify.frd");
        result = parseAndVisit(resourceAsStream);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
        Assert.assertEquals(2, result.getNotificationsRule().size());
    }
}
