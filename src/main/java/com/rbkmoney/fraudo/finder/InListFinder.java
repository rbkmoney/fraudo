package com.rbkmoney.fraudo.finder;

import com.rbkmoney.fraudo.constant.CheckedField;

public interface InListFinder {

    Boolean findInList(CheckedField field, String value);

}
