package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface UniqueValueAggregator {

    Integer countUniqueValue(CheckedField countField, CheckedField onField);

}
