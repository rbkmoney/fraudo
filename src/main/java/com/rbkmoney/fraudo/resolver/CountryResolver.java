package com.rbkmoney.fraudo.resolver;

public interface CountryResolver<T> {

    String UNKNOWN_VALUE = "unknown";

    String resolveCountry(T checkedField, String value);

}
