package com.rbkmoney.fraudo.p2p;

import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.utils.ResultUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;

public class SumP2PTest extends AbstractP2PTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sumTest() throws Exception {
        InputStream resourceAsStream = SumP2PTest.class.getResourceAsStream("/rules/p2p/sum.frd");
        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(10500.60);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(1, ResultUtils.getNotifications(result).size());

        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(90.0);

        result = invokeParse(parseContext);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(0, ResultUtils.getNotifications(result).size());
    }

    @Test
    public void sumGroupByTest() throws Exception {
        InputStream resourceAsStream = SumP2PTest.class.getResourceAsStream("/rules/p2p/sumGroupBy.frd");
        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(10500.60);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(1, ResultUtils.getNotifications(result).size());

        Mockito.when(sumAggregator.sum(anyObject(), any(), any(), any())).thenReturn(90.0);

        result = invokeParse(parseContext);
        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(0, ResultUtils.getNotifications(result).size());
    }

}
