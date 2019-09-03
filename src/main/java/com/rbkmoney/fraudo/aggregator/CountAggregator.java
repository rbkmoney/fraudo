package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface CountAggregator {

    @Deprecated
    Integer count(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Integer count(CheckedField checkedField, FraudModel model, TimeWindow timeWindow, List<CheckedField> fields);

    @Deprecated
    Integer countSuccess(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Integer countSuccess(CheckedField checkedField, FraudModel model, TimeWindow timeWindow);

    @Deprecated
    Integer countError(CheckedField checkedField, FraudModel model, Long timeInMinutes, String errorCode);

    Integer countError(CheckedField checkedField, FraudModel model, TimeWindow timeWindow, String errorCode);

}
