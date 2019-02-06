package com.rbkmoney.fraudo.resolver;

public interface CountryResolver {

    String UNKNOWN_VALUE = "unknown";

    String resolveCountryByIp(String value);

}
