package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;
import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface UniqueValueAggregator {

    @Deprecated
    Integer countUniqueValue(CheckedField countField, FraudModel fraudModel, CheckedField onField, Long time);

    Integer countUniqueValue(CheckedField countField, FraudModel fraudModel, CheckedField onField, TimeWindow timeWindow, List<CheckedField> fields);

}
