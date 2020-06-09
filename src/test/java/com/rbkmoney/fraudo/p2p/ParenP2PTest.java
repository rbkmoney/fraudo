package com.rbkmoney.fraudo.p2p;

import com.rbkmoney.fraudo.FraudoP2PParser;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class ParenP2PTest extends AbstractP2PTest {

    public static final String FIRST_RULE_INDEX = "0";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void countCardTokenTest() throws Exception {
        InputStream resourceAsStream = ParenP2PTest.class.getResourceAsStream("/rules/p2p/paren.frd");
        when(countAggregator.count(anyObject(), any(), any(), any())).thenReturn(10);
        FraudoP2PParser.ParseContext parseContext = getParseContext(resourceAsStream);
        ResultModel result = invokeParse(parseContext);

        assertEquals(ResultStatus.DECLINE, result.getResultStatus());
        verify(countAggregator, times(2)).count(anyObject(), any(), any(), any());
    }

}
