package com.rbkmoney.fraudo;

import com.rbkmoney.fraudo.constant.ResultStatus;
import com.rbkmoney.fraudo.model.PaymentModel;
import com.rbkmoney.fraudo.model.ResultModel;
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

public class RealTimerTest extends AbstractFraudoTest {

    public static final long TIME_CALL_AGGR_FUNC = 200L;
    public static final long MILLISTIME_FAST_FUNC = 10L;
    public static final long TIME_CALLING = 200L;

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

        PaymentModel model = new PaymentModel();
        model.setAmount(MILLISTIME_FAST_FUNC);
        model.setBin("444443");

        long start = System.currentTimeMillis();
        ResultModel result = invoke(parseContext, model);
        long executionTime = System.currentTimeMillis() - start;
        assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
        assertEquals(0, countDownLatch.getCount());
        assertTrue(executionTime < TIME_CALL_AGGR_FUNC + 1 + TIME_CALLING);

        System.out.println("executionTime=" + executionTime);
    }

    @Test
    public void timingWithSuccessTest() throws Exception {
        InputStream resourceAsStream = RealTimerTest.class.getResourceAsStream("/rules/sum_and_count_template.frd");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        mockAggr(countDownLatch);

        com.rbkmoney.fraudo.FraudoParser.ParseContext parseContext = getParseContext(resourceAsStream);

        PaymentModel model = new PaymentModel();
        model.setAmount(MILLISTIME_FAST_FUNC);
        model.setBin("444443");

        long start = System.currentTimeMillis();
        ResultModel result = invoke(parseContext, model);
        long executionTime = System.currentTimeMillis() - start;

        System.out.println("executionTime=" + executionTime);
        System.out.println("result=" + result.getRuleChecked());

        assertEquals(ResultStatus.ACCEPT, result.getResultStatus());
        assertTrue(executionTime < TIME_CALL_AGGR_FUNC * 4 + TIME_CALLING);
    }

    private void mockAggr(CountDownLatch countDownLatch) {
        when(countAggregator.count(any(), any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    countDownLatch.countDown();
                    return 1;
                });
        when(countAggregator.countSuccess(any(), any(), any(), any()))
                .thenAnswer((Answer<Integer>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    countDownLatch.countDown();
                    return 1;
                });

        when(sumAggregator.sum(any(), any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    return 10000.0;
                });
        when(sumAggregator.sumSuccess(any(), any(), any(), any()))
                .thenAnswer((Answer<Double>) invocationOnMock -> {
                    Thread.sleep(TIME_CALL_AGGR_FUNC);
                    return 10000.0;
                });

        when(whiteListFinder.findInList(anyList(), anyObject()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(MILLISTIME_FAST_FUNC);
                    return false;
                });
        when(greyListFinder.findInList(anyList(), anyObject()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(MILLISTIME_FAST_FUNC);
                    return false;
                });
        when(blackListFinder.findInList(anyList(), anyObject()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Thread.sleep(MILLISTIME_FAST_FUNC);
                    return false;
                });
        when(countryResolver.resolveCountry(anyObject(), anyString()))
                .thenAnswer((Answer<String>) invocationOnMock -> "RUS");
    }
}
