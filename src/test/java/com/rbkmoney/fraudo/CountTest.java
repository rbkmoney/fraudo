package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.FraudoPaymentParser.ParseContext;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.utils.ResultUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class CountTest extends AbstractPaymentTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void countTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        when(countPaymentAggregator.countError(anyObject(), any(), any(), anyString(), any())).thenReturn(6);
        when(countPaymentAggregator.countSuccess(anyObject(), any(), any(), any())).thenReturn(4);
        when(countPaymentAggregator.countChargeback(anyObject(), any(), any(), any())).thenReturn(0);
        when(countPaymentAggregator.countRefund(anyObject(), any(), any(), any())).thenReturn(1);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("0", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());

        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(9);
        when(countPaymentAggregator.countError(anyObject(), any(), any(), anyString(), any())).thenReturn(6);
        when(countPaymentAggregator.countSuccess(anyObject(), any(), any(), any())).thenReturn(6);

        result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void countCargeRefundTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count_chargeback_refund.frd");
        when(countPaymentAggregator.countChargeback(anyObject(), any(), any(), any())).thenReturn(3);
        when(countPaymentAggregator.countRefund(anyObject(), any(), any(), any())).thenReturn(5);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("0", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());
    }

    @Test
    public void countGroupByTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/countGroupBy.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("0", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());

        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
    }

    @Test
    public void countTimeWindowGroupByTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/countTimeWindowGroupBy.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("0", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());

        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());

        resourceAsStream = CountTest.class.getResourceAsStream("/rules/countTimeWindowGroupBy_2.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        parseContext = getParseContext(resourceAsStream);
        result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals("0", ResultUtils.findFirstNotNotifyStatus(result).get().getRuleChecked());
    }

    @Test
    public void countCardTokenTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count_card_token.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        verify(countPaymentAggregator, times(1)).count(anyObject(), any(), any(), any());
    }

    @Test
    public void countPhoneTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count_phone.frd");
        when(countPaymentAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        verify(countPaymentAggregator, times(1)).count(anyObject(), any(), any(), any());
    }

}
