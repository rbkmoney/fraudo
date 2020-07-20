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

import static org.junit.Assert.assertFalse;
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
        Assert.assertEquals(ResultStatus.DECLINE, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());

        resourceAsStream = ListTest.class.getResourceAsStream("/rules/blacklist.frd");
        parseContext = getParseContext(resourceAsStream);
        Mockito.when(inListFinder.findInBlackList(anyList(), anyObject())).thenReturn(true);
        result = invokeParse(parseContext);

        assertFalse(ResultUtils.findFirstNotNotifyStatus(result).isPresent());
        Assert.assertEquals(1, ResultUtils.getNotifications(result).size());
    }

    @Test
    public void greyListTest() throws Exception {
        InputStream resourceAsStream = ListTest.class.getResourceAsStream("/rules/greyList.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(inListFinder.findInGreyList(anyList(), anyObject())).thenReturn(true);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }

    @Test
    public void namingListTest() throws Exception {
        InputStream resourceAsStream = ListTest.class.getResourceAsStream("/rules/namingList.frd");
        ParseContext parseContext = getParseContext(resourceAsStream);
        Mockito.when(inListFinder.findInList(anyString(), anyList(), anyObject())).thenReturn(true);
        ResultModel result = invokeParse(parseContext);
        Assert.assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
    }
}
