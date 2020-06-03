package com.rbkmoney.fraudo.aggragator;

import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface UniqueValueAggregator<T, U> {

    Integer countUniqueValue(U countField, T payoutModel, U onField, TimeWindow timeWindow, List<U> fields);

}
