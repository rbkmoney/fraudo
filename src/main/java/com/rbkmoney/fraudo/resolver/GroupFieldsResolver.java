package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.FraudoParser;

import java.util.List;

public interface GroupFieldsResolver<T> {

    List<T> resolve(FraudoParser.Group_byContext groupByContext);

}
