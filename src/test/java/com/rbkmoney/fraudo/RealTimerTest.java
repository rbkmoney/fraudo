package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.ResultModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Matchers.*;

public class RealTimerTest extends AbstractFraudoTest {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void timingTest() throws Exception {
        InputStream resourceAsStream = RealTimerTest.class.getResourceAsStream("/rules/real_template.frd");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        mockAggr(countDownLatch);

        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);

        FraudModel model = new FraudModel();
        model.setAmount(10L);
        model.setBin("444443");

        long start = System.currentTimeMillis();
        ResultModel result = invoke(parseContext, model);
        long executionTime = System.currentTimeMillis() - start;
        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
        Assert.assertEquals(0, countDownLatch.getCount());
        Assert.assertTrue(executionTime < 300);

        System.out.println("executionTime=" + executionTime);
    }

    @Test
    public void timingWithSuccessTest() throws Exception {
        InputStream resourceAsStream = RealTimerTest.class.getResourceAsStream("/rules/sum_and_count_template.frd");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        mockAggr(countDownLatch);

        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);

        FraudModel model = new FraudModel();
        model.setAmount(10L);
        model.setBin("444443");

        long start = System.currentTimeMillis();
        ResultModel result = invoke(parseContext, model);
        long executionTime = System.currentTimeMillis() - start;

        System.out.println("executionTime=" + executionTime);
        System.out.println("result=" + result.getRuleChecked());

        Assert.assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
        Assert.assertTrue(executionTime < 900);
    }

    private void mockAggr(CountDownLatch countDownLatch) {
        Mockito.when(countAggregator.count(any(), any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(200L);
                    countDownLatch.countDown();
                    return 1;
                });
        Mockito.when(countAggregator.count(any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(200L);
                    countDownLatch.countDown();
                    return 1;
                });
        Mockito.when(countAggregator.countSuccess(any(), any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(200L);
                    countDownLatch.countDown();
                    return 1;
                });
        Mockito.when(countAggregator.countSuccess(any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(200L);
                    countDownLatch.countDown();
                    return 1;
                });

        Mockito.when(sumAggregator.sum(any(), any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(200L);
                    return 10000.0;
                });
        Mockito.when(sumAggregator.sum(any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(200L);
                    return 10000.0;
                });
        Mockito.when(sumAggregator.sumSuccess(any(), any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(200L);
                    return 10000.0;
                });
        Mockito.when(sumAggregator.sumSuccess(any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(200L);
                    return 10000.0;
                });

        Mockito.when(whiteListFinder.findInList(anyString(), anyString(), anyList(), anyList()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(10L);
                    return false;
                });
        Mockito.when(greyListFinder.findInList(anyString(), anyString(), anyList(), anyList()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(10L);
                    return false;
                });
        Mockito.when(greyListFinder.findInList(anyString(), anyString(), anyObject(), anyString()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(10L);
                    return false;
                });
        Mockito.when(blackListFinder.findInList(anyString(), anyString(), anyObject(), anyString()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(10L);
                    return false;
                });
        Mockito.when(blackListFinder.findInList(anyString(), anyString(), anyList(), anyList()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(10L);
                    return false;
                });
        Mockito.when(countryResolver.resolveCountry(anyObject(), anyString()))
                .thenAnswer((Answer<String>) invocationOnMock -> "RUS");
    }
}
