package com.rbkmoney.fraudo.p2p.aggragator;

import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface SumP2PAggregator<T, U> {

    Double sum(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

}
