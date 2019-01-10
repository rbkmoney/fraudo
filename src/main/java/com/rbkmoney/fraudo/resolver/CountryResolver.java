package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface CountryResolver {

    String UNKNOWN_VALUE = "unknown";

    String resolveCountry(CheckedField checkedField, String value);

}
