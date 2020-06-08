package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.FraudoPaymentParser.ParseContext;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;

import static org.mockito.Matchers.*;

public class ListTest extends AbstractPaymentTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whiteBlackListTest() throws Exception {
        InputStream resourceAsStream = ListTest.class.getResourceAsStream("/rules/whitelist.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(inListFinder.findInWhiteList(anyList(), anyObject())).thenReturn(true);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.DECLINE, result.getResultStatus());

        resourceAsStream = ListTest.class.getResourceAsStream("/rules/blacklist.frd");
        parseContext = getParseContext(resourceAsStream);
        Mockito.when(inListFinder.findInBlackList(anyList(), anyObject())).thenReturn(true);
        result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.NORMAL, result.getResultStatus());
        Assert.assertEquals(1, result.getNotificationsRule().size());
    }

    @Test
    public void greyListTest() throws Exception {
        InputStream resourceAsStream = ListTest.class.getResourceAsStream("/rules/greyList.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(inListFinder.findInGreyList(anyList(), anyObject())).thenReturn(true);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
    }

    @Test
    public void namingListTest() throws Exception {
        InputStream resourceAsStream = ListTest.class.getResourceAsStream("/rules/namingList.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(inListFinder.findInList(anyString(), anyList(), anyObject())).thenReturn(true);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
    }
}
