package com.rbkmoney.fraudo.p2p;

import com.rbkmoney.fraudo.CustomTest;
import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.FraudoPaymentParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.test.model.P2PModel;
import com.rbkmoney.fraudo.test.model.PaymentModel;
import com.rbkmoney.fraudo.utils.ResultUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class CustomP2PTest extends AbstractP2PTest {

    public static final String TEST_GMAIL_RU = "test@gmail.ru";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void threeDsTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/three_ds.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.THREE_DS, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void highRiskTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/highRisk.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.HIGH_RISK, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void notifyTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/notify.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        assertEquals(1, ResultUtils.getNotifications(result).size());
    }

    @Test
    public void declineTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/decline.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("test_11", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());
    }

    @Test
    public void acceptTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/accept.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void ruleIsNotFireTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/rule_is_not_fire.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
    }

    @Test(expected = UnknownResultException.class)
    public void notImplOperatorTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/unknownResult.frd");
        parseAndVisit(resourceAsStream);
    }

    @Test
    public void inTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/in.frd");
        when(countryResolver.resolveCountry(any(), anyString())).thenReturn("SD");

        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        P2PModel model = new P2PModel();
        model.setEmail(TEST_GMAIL_RU);
        model.setBin("123213");
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void inCurrencyTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/p2p/in_currency.frd");

        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        P2PModel model = new P2PModel();
        model.setCurrency("EUR");
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void inCountryTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/in_country.frd");
        when(countryResolver.resolveCountry(any(), anyString())).thenReturn("RU");

        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        P2PModel model = new P2PModel();
        model.setAmount(500L);
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void amountTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/amount.frd");
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        P2PModel model = new P2PModel();
        model.setAmount(56L);
        model.setCurrency("RUB");
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());

        model.setCurrency("USD");
        result = invoke(parseContext, model);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
    }

    @Test
    public void catchTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/catch.frd");
        when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any()))
                .thenThrow(new UnknownResultException("as"));
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void likeTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/like.frd");
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        P2PModel model = new P2PModel();
        model.setEmail(TEST_GMAIL_RU);
        model.setBin("553619");
        model.setPan("9137");
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());

        model.setPan("9111");
        result = invoke(parseContext, model);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
    }

    @Test
    public void inNotTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/in_not.frd");
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        P2PModel model = new P2PModel();
        model.setEmail(TEST_GMAIL_RU);
        ResultModel result = invoke(parseContext, model);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
    }

    @Test
    public void uniqCountTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/count_uniq.frd");
        when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any())).thenReturn(2);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void uniqCountGroupByTest() throws Exception {
        InputStream resourceAsStream =
                CustomP2PTest.class.getResourceAsStream("/rules/p2p/count_uniqGroupBy_window.frd");
        when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any())).thenReturn(2);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        verify(uniqueValueAggregator, times(1)).countUniqueValue(any(), any(), any(), any(), any());
    }

    @Test
    public void eqCountryTest() throws Exception {
        InputStream resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/eq_country.frd");

        when(countryResolver.resolveCountry(any(), anyString())).thenReturn("RU");

        ResultModel result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(1, ResultUtils.getNotifications(result).size());

        when(countryResolver.resolveCountry(any(), anyString())).thenReturn("US");
        resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/eq_country.frd");
        result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(0, ResultUtils.getNotifications(result).size());

        resourceAsStream = CustomP2PTest.class.getResourceAsStream("/rules/p2p/accept_with_notify.frd");
        result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        Assert.assertEquals(2, ResultUtils.getNotifications(result).size());
    }
}
