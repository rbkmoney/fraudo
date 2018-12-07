package com.rbkmoney.fraudo.resolver;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface CountryResolver {

    String resolveCountry(CheckedField checkedField, String value);

}
