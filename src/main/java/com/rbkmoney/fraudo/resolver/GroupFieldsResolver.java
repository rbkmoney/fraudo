package com.rbkmoney.fraudo.resolver;

import java.util.List;

public interface GroupFieldsResolver<T, R> {

    List<R> resolve(T groupByContext);

}
