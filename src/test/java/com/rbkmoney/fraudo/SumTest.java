package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.mockito.Matchers.*;

public class SumTest extends AbstractFraudoTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sumTest() throws Exception {
        InputStream resourceAsStream = SumTest.class.getResourceAsStream("/rules/sum.frd");
        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(10500.60);
        Mockito.when(sumAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(524.0);
        Mockito.when(sumAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(4.0);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(1, result.getNotificationsRule().size());

        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(90.0);
        Mockito.when(sumAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(504.0);
        Mockito.when(sumAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(501.0);

        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(0, result.getNotificationsRule().size());
    }

    @Test
    public void sumGroupByTest() throws Exception {
        InputStream resourceAsStream = SumTest.class.getResourceAsStream("/rules/sumGroupBy.frd");
        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(10500.60);
        Mockito.when(sumAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(524.0);
        Mockito.when(sumAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(4.0);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(1, result.getNotificationsRule().size());

        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(90.0);
        Mockito.when(sumAggregator.sumError(anyObject(), any(), any(), anyString(), any())).thenReturn(504.0);
        Mockito.when(sumAggregator.sumSuccess(anyObject(), any(), any(), any())).thenReturn(501.0);

        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(0, result.getNotificationsRule().size());
    }

}
