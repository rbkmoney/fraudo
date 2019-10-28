package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface CountAggregator<T> {

    @Deprecated
    Integer count(CheckedField checkedField, T model, Long timeInMinutes);

    Integer count(CheckedField checkedField, T model, TimeWindow timeWindow, List<CheckedField> fields);

    @Deprecated
    Integer countSuccess(CheckedField checkedField, T model, Long timeInMinutes);

    Integer countSuccess(CheckedField checkedField, T model, TimeWindow timeWindow, List<CheckedField> fields);

    @Deprecated
    Integer countError(CheckedField checkedField, T model, Long timeInMinutes, String errorCode);

    Integer countError(CheckedField checkedField, T model, TimeWindow timeWindow, String errorCode, List<CheckedField> fields);

}
