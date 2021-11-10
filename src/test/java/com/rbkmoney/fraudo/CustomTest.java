package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.FraudoPaymentParser.ParseContext;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.test.model.PaymentModel;
import com.rbkmoney.fraudo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@Slf4j
public class CustomTest extends AbstractPaymentTest {

    public static final String TEST_GMAIL_RU = "test@gmail.ru";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void threeDsTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/three_ds.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.THREE_DS, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void highRiskTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/highRisk.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.HIGH_RISK, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void notifyTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/notify.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        assertEquals(1, ResultUtils.getNotifications(result).size());
    }

    @Test
    public void declineTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/decline.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("test_11", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());
    }

    @Test
    public void declineAndNotifyTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/declineAndNotify.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.DECLINE_AND_NOTIFY, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("test_11", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());
        Assert.assertEquals(1, ResultUtils.getNotifications(result).size());
    }

    @Test
    public void acceptTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/accept.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void ruleIsNotFireTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/rule_is_not_fire.frd");
        ResultModel result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
    }

    @Test(expected = UnknownResultException.class)
    public void notImplOperatorTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/unknownResult.frd");
        parseAndVisit(resourceAsStream);
    }

    @Test
    public void inTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/in.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
        model.setEmail(TEST_GMAIL_RU);
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void inCountryTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/in_country.frd");
        when(countryResolver.resolveCountry(any(), anyString())).thenReturn("RU");

        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
        model.setAmount(500L);
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void inCurrencyTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/in_currency.frd");

        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
        model.setCurrency("EUR");
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void tokenProviderTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/is_mobile.frd");
        when(paymentModelPaymentTypeResolver.isMobile(any())).thenReturn(true);

        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void payerTypeTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/is_recurrent.frd");
        when(paymentModelPaymentTypeResolver.isRecurrent(any())).thenReturn(true);

        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
        ResultModel result = invoke(parseContext, model);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void amountTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/amount.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
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
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/catch.frd");
        when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any())).thenThrow(new UnknownResultException("as"));
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void likeTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/like.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
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
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/in_not.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        PaymentModel model = new PaymentModel();
        model.setEmail(TEST_GMAIL_RU);
        ResultModel result = invoke(parseContext, model);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
    }

    @Test
    public void uniqCountTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/count_uniq.frd");
        when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any())).thenReturn(2);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void uniqCountGroupByTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/count_uniqGroupBy_window.frd");
        when(uniqueValueAggregator.countUniqueValue(any(), any(), any(), any(), any())).thenReturn(2);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        verify(uniqueValueAggregator, times(1)).countUniqueValue(any(), any(), any(), any(), any());
    }

    @Test
    public void eqCountryTest() throws Exception {
        InputStream resourceAsStream = CustomTest.class.getResourceAsStream("/rules/eq_country.frd");

        when(countryResolver.resolveCountry(any(), anyString())).thenReturn("RU");

        ResultModel result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        assertEquals(1, ResultUtils.getNotifications(result).size());

        when(countryResolver.resolveCountry(any(), anyString())).thenReturn("US");
        resourceAsStream = CustomTest.class.getResourceAsStream("/rules/eq_country.frd");
        result = parseAndVisit(resourceAsStream);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());

        resourceAsStream = CustomTest.class.getResourceAsStream("/rules/accept_with_notify.frd");
        result = parseAndVisit(resourceAsStream);
        assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals(2, ResultUtils.getNotifications(result).size());
    }
}
