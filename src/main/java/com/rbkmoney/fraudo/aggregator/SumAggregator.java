package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface SumAggregator {

    @Deprecated
    Double sum(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Double sum(CheckedField checkedField, FraudModel model, TimeWindow timeWindow, List<CheckedField> fields);

    @Deprecated
    Double sumSuccess(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Double sumSuccess(CheckedField checkedField, FraudModel model, TimeWindow timeWindow, List<CheckedField> fields);

    @Deprecated
    Double sumError(CheckedField checkedField, FraudModel model, Long timeInMinutes, String errorCode);

    Double sumError(CheckedField checkedField, FraudModel model, TimeWindow timeWindow, String errorCode, List<CheckedField> fields);

}
