package com.rbkmoney.fraudo.aggregator;

import com.rbkmoney.fraudo.constant.CheckedField;
import com.rbkmoney.fraudo.model.FraudModel;

public interface CountAggregator {

    Integer count(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Integer countSuccess(CheckedField checkedField, FraudModel model, Long timeInMinutes);

    Integer countError(CheckedField checkedField, FraudModel model, Long timeInMinutes, String errorCode);

}
