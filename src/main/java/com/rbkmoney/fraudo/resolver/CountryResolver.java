package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface CountryResolver<T> {

    String UNKNOWN_VALUE = "unknown";

    String resolveCountry(T checkedField, String value);

}
