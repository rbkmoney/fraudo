package com.rbkmoney.fraudo.finder;


import com.rbkmoney.fraudo.model.Pair;

import java.util.List;

public interface InNamingListFinder<T, U> {

    Boolean findInList(String name, List<Pair<U, String>> fields, T model);

}
