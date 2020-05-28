package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface CountAggregator<T, U> {

    Integer count(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

    Integer countSuccess(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

    Integer countError(U checkedField, T model, TimeWindow timeWindow, String errorCode, List<U> fields);

    Integer countChargeback(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

    Integer countRefund(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

}
