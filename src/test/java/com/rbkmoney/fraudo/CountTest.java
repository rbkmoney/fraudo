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

public class CountTest extends AbstractFraudoTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void countTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count.frd");
        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        Mockito.when(countAggregator.countError(anyObject(), any(), any(), anyString(), any())).thenReturn(6);
        Mockito.when(countAggregator.countSuccess(anyObject(), any(), any(), any())).thenReturn(4);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        Assert.assertEquals("1", result.getRuleChecked());

        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(9);
        Mockito.when(countAggregator.countError(anyObject(), any(), any(), anyString(), any())).thenReturn(6);
        Mockito.when(countAggregator.countSuccess(anyObject(), any(), any(), any())).thenReturn(6);

        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
    }

    @Test
    public void countGroupByTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/countGroupBy.frd");
        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        Assert.assertEquals("1", result.getRuleChecked());

        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
    }

    @Test
    public void countTimeWindowGroupByTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/countTimeWindowGroupBy.frd");
        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        Assert.assertEquals("1", result.getRuleChecked());

        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
    }

    @Test
    public void countCardTokenTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count_card_token.frd");
        Mockito.when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());
    }

}
