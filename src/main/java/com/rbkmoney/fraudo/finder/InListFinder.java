package com.rbkmoney.fraudo.finder;


import com.rbkmoney.fraudo.model.Pair;

import java.util.List;

public interface InListFinder<T, U> {

    Boolean findInList(List<Pair<T, U>> fields, T model);

}
