package com.rbkmoney.fraudo.finder;

import com.sun.tools.javac.util.Pair;

import java.util.List;

public interface InListFinder<T, U> {

    Boolean findInList(List<Pair<T, U>> fields, T model);

}
