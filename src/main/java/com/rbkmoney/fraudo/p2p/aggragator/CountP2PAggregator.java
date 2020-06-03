package com.rbkmoney.fraudo.p2p.aggragator;

import com.rbkmoney.fraudo.model.TimeWindow;

import java.util.List;

public interface CountP2PAggregator<T, U> {

    Integer count(U checkedField, T model, TimeWindow timeWindow, List<U> fields);

}
