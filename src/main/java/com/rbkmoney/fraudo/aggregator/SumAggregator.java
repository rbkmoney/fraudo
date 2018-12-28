package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;

public interface SumAggregator {

    Double sum(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Double sumSuccess(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Double sumError(CheckedField checkedField, FraudModel model, Long timeInMinutes, String errorCode);

}
