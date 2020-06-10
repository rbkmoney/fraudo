package com.rbkmoney.fraudo.resolver;

public interface CountryResolver<T> {

    String resolveCountry(T checkedField, String value);

}
