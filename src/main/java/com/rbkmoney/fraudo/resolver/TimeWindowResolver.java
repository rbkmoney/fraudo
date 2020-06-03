package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.model.TimeWindow;

public interface TimeWindowResolver<T> {

    TimeWindow resolve(T ctx);

}
