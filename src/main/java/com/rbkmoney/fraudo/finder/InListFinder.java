package com.rbkmoney.fraudo.finder;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface InListFinder {

    Boolean findInList(String partyId, String shopId, CheckedField field, String value);

}
