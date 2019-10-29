package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface SumAggregator<T, U> {

    Double sum(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

    Double sumSuccess(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

    Double sumError(U checkedField, T model, TimeWindow timeWindow, String errorCode, List<U> fields);

}
