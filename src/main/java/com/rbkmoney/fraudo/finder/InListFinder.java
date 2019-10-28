package com.rbkmoney.fraudo.finder;

import com.rbkmoney.fraudo.constant.CheckedField;

import java.util.List;

public interface InListFinder {

    Boolean findInList(String partyId, String shopId, CheckedField field, String value);

    Boolean findInList(String partyId, String shopId, List<CheckedField> fields, List<String> value);

    Boolean findInList(CheckedField field, String value, String... ids);

}
