package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface SumAggregator {

    Double sum(CheckedField checkedField, String email, Long timeInMinutes);

    Double sumSuccess(CheckedField checkedField, String valueField, Long timeInMinutes);

    Double sumError(CheckedField checkedField, String valueField, Long timeInMinutes, String errorCode);

}
