package com.rbkmoney.fraudo.p2p;

import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class CountP2PTest extends AbstractP2PTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void countTest() throws Exception {
        InputStream resourceAsStream = CountP2PTest.class.getResourceAsStream("/rules/p2p/count.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());

        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(14);

        result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
    }

    @Test
    public void countGroupByTest() throws Exception {
        InputStream resourceAsStream = CountP2PTest.class.getResourceAsStream("/rules/p2p/countGroupBy.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());

        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        assertEquals(ResultStatus.NORMAL, result.getResultStatus());
    }

    @Test
    public void countTimeWindowGroupByTest() throws Exception {
        InputStream resourceAsStream = CountP2PTest.class.getResourceAsStream("/rules/p2p/countTimeWindowGroupBy.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());

        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(1);
        result = invokeParse(parseContext);
        assertEquals(ResultStatus.NORMAL, result.getResultStatus());

        resourceAsStream = CountP2PTest.class.getResourceAsStream("/rules/p2p/countTimeWindowGroupBy_2.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        parseContext = getParseContext(resourceAsStream);
        result = invokeParse(parseContext);
        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        assertEquals("1", result.getRuleChecked());
    }

    @Test
    public void countCardTokenTest() throws Exception {
        InputStream resourceAsStream = CountP2PTest.class.getResourceAsStream("/rules/p2p/count_card_token.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        verify(countAggregator, times(1)).count(anyObject(), any(), any(), any());
    }

}
