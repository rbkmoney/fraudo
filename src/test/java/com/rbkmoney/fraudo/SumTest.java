package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.FraudoPaymentParser.ParseContext;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.utils.ResultUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.mockito.Matchers.*;

public class SumTest extends AbstractPaymentTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sumTest() throws Exception {
        InputStream resourceAsStream = SumTest.class.getResourceAsStream("/rules/sum.frd");
        Mockito.when(sumPaymentAggregator.sum(anyObject(), any(), any(), any())).thenReturn(10500.60);
        Mockito.when(sumPaymentAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(524.0);
        Mockito.when(sumPaymentAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(4.0);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(1, ResultUtils.getNotifications(result).size());

        Mockito.when(sumPaymentAggregator.sum(anyObject(), any(), any(), any())).thenReturn(90.0);
        Mockito.when(sumPaymentAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(504.0);
        Mockito.when(sumPaymentAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(501.0);

        result = invokeParse(parseContext);
        Assert.assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(0, ResultUtils.getNotifications(result).size());
    }

    @Test
    public void sumChargeRefundTest() throws Exception {
        InputStream resourceAsStream = SumTest.class.getResourceAsStream("/rules/sum_chargeback_refund.frd");
        Mockito.when(sumPaymentAggregator.sumChargeback(anyObject(), any(), any(), any())).thenReturn(10000.60);
        Mockito.when(sumPaymentAggregator.sumRefund(anyObject(), any(), any(), any())).thenReturn(10000.60);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void sumGroupByTest() throws Exception {
        InputStream resourceAsStream = SumTest.class.getResourceAsStream("/rules/sumGroupBy.frd");
        Mockito.when(sumPaymentAggregator.sum(anyObject(), any(), any(), any())).thenReturn(10500.60);
        Mockito.when(sumPaymentAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(524.0);
        Mockito.when(sumPaymentAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(4.0);
        ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        Assert.assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(1, ResultUtils.getNotifications(result).size());

        Mockito.when(sumPaymentAggregator.sum(anyObject(), any(), any(), any())).thenReturn(90.0);
        Mockito.when(sumPaymentAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(504.0);
        Mockito.when(sumPaymentAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(501.0);

        result = invokeParse(parseContext);
        Assert.assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(0, ResultUtils.getNotifications(result).size());
    }

}
