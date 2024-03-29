package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.FraudoPaymentParser.ParseContext;
import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.ResultModel;
import com.rbkmoney.fraudo.test.model.PaymentModel;
import com.rbkmoney.fraudo.utils.ResultUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class RealTimerTest extends AbstractPaymentTest {

    public static final long TIME_CALL_AGGR_FUNC = 200L;
    public static final long MILLISTIME_FAST_FUNC = 10L;
    public static final long TIME_CALLING = 300L;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void timingTest() throws Exception {
        InputStream resourceAsStream = RealTimerTest.class.getResourceAsStream("/rules/payment_template.frd");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        mockAggr(countDownLatch);

        ParseContext parseContext = getParseContext(resourceAsStream);

        PaymentModel model = new PaymentModel();
        model.setAmount(MILLISTIME_FAST_FUNC);
        model.setBin("444443");

        long start = System.currentTimeMillis();
        ResultModel result = invoke(parseContext, model);
        long executionTime = System.currentTimeMillis() - start;

        Assert.assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertEquals(0, countDownLatch.getCount());
        assertTrue(executionTime < TIME_CALL_AGGR_FUNC + 1 + TIME_CALLING);

        System.out.println("timingTest.executionTime=" + executionTime);

        result = invokeFullVisitor(parseContext, model);
        assertEquals(2, result.getRuleResults().size());
    }

    @Test
    public void timingWithSuccessTest() throws Exception {
        InputStream resourceAsStream = RealTimerTest.class.getResourceAsStream("/rules/sum_and_count_template.frd");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        mockAggr(countDownLatch);

        ParseContext parseContext = getParseContext(resourceAsStream);

        PaymentModel model = new PaymentModel();
        model.setAmount(MILLISTIME_FAST_FUNC);
        model.setBin("444443");

        long start = System.currentTimeMillis();
        ResultModel result = invoke(parseContext, model);
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("timingWithSuccessTest.executionTime=" + executionTime);

        Assert.assertEquals(ResultStatus.ACCEPT, ResultUtils.findFirstNotNotifyStatus(result).get().getResultStatus());
        assertTrue(executionTime < TIME_CALL_AGGR_FUNC * 4 + TIME_CALLING);
    }

    private void mockAggr(CountDownLatch countDownLatch) {
        when(countPaymentAggregator.count(any(), any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    countDownLatch.countDown();
                    return 1;
                });
        when(countPaymentAggregator.countSuccess(any(), any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    countDownLatch.countDown();
                    return 1;
                });

        when(sumPaymentAggregator.sum(any(), any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    return 10000.0;
                });
        when(sumPaymentAggregator.sumSuccess(any(), any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    return 10000.0;
                });

        when(inListFinder.findInWhiteList(anyList(), anyObject()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(MILLISTIME_FAST_FUNC);
                    return false;
                });
        when(inListFinder.findInGreyList(anyList(), anyObject()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(MILLISTIME_FAST_FUNC);
                    return false;
                });
        when(inListFinder.findInBlackList(anyList(), anyObject()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(MILLISTIME_FAST_FUNC);
                    return false;
                });

        when(countryResolver.resolveCountry(anyObject(), anyString()))
                .thenAnswer((Answer<String>) invocationOnMock -> "RUS");
    }
}
