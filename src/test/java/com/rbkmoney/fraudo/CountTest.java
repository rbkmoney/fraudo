package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class CountTest extends AbstractFraudoTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void countTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        when(countAggregator.countError(anyObject(), any(), any(), anyString(), any())).thenReturn(6);
        when(countAggregator.countSuccess(anyObject(), any(), any(), any())).thenReturn(4);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());

        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(9);
        when(countAggregator.countError(anyObject(), any(), any(), anyString(), any())).thenReturn(6);
        when(countAggregator.countSuccess(anyObject(), any(), any(), any())).thenReturn(6);

        result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
    }

    @Test
    public void countGroupByTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/countGroupBy.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());

        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        assertEquals(ResultStatus.NORMAL, result.getResultStatus());
    }

    @Test
    public void countTimeWindowGroupByTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/countTimeWindowGroupBy.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());

        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        assertEquals(ResultStatus.NORMAL, result.getResultStatus());

        resourceAsStream = CountTest.class.getResourceAsStream("/rules/countTimeWindowGroupBy_2.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        parseContext = getParseContext(resourceAsStream);
        result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());
    }

    @Test
    public void countCardTokenTest() throws Exception {
        InputStream resourceAsStream = CountTest.class.getResourceAsStream("/rules/count_card_token.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        verify(countAggregator, times(1)).count(anyObject(), any(), any(), any());
    }

}
