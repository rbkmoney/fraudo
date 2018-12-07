package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface CountAggregator {

    Integer count(CheckedField checkedField, String valueField, Long timeInMinutes);

    Integer countSuccess(CheckedField checkedField, String valueField, Long timeInMinutes);

    Integer countError(CheckedField checkedField, String valueField, Long timeInMinutes, String errorCode);

}
