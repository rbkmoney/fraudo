package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.exception.UnknownResultException;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.mockito.Matchers.*;

public class ListTest extends AbstractFraudoTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whiteBlackListTest() throws Exception {
        InputStream resourceAsStream = ListTest.class.getResourceAsStream("/rules/whitelist.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(whiteListFinder.findInList(anyString(), anyString(), anyList(), anyList())).thenReturn(true);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());

        resourceAsStream = ListTest.class.getResourceAsStream("/rules/blacklist.frd");
        parseContext = getParseContext(resourceAsStream);
        Mockito.when(blackListFinder.findInList(anyString(), anyString(), anyList(), anyList())).thenReturn(true);
        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(1, result.getNotificationsRule().size());
    }

    @Test
    public void greyListTest() throws Exception {
        InputStream resourceAsStream = ListTest.class.getResourceAsStream("/rules/greyList.frd");
        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(greyListFinder.findInList(anyString(), anyString(), anyList(), anyList())).thenReturn(true);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
    }
}
