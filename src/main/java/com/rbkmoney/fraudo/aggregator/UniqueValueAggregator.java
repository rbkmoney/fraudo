package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;

public interface UniqueValueAggregator {

    Integer countUniqueValue(CheckedField countField, FraudModel fraudModel, CheckedField onField, Long time);

}
