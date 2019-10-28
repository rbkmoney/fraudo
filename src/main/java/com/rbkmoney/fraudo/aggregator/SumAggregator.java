package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface SumAggregator<T> {

    @Deprecated
    Double sum(CheckedField checkedField, T model, Long timeInMinutes);

    Double sum(CheckedField checkedField, T model, TimeWindow timeWindow, List<CheckedField> fields);

    @Deprecated
    Double sumSuccess(CheckedField checkedField, T model, Long timeInMinutes);

    Double sumSuccess(CheckedField checkedField, T model, TimeWindow timeWindow, List<CheckedField> fields);

    @Deprecated
    Double sumError(CheckedField checkedField, T model, Long timeInMinutes, String errorCode);

    Double sumError(CheckedField checkedField, T model, TimeWindow timeWindow, String errorCode, List<CheckedField> fields);

}
