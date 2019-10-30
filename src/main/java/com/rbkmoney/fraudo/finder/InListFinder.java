package com.rbkmoney.fraudo.finder;


import com.rbkmoney.fraudo.model.Pair;

import java.util.List;

public interface InListFinder<T, U> {

    Boolean findInBlackList(List<Pair<U, String>> fields, T model);

    Boolean findInWhiteList(List<Pair<U, String>> fields, T model);

    Boolean findInGreyList(List<Pair<U, String>> fields, T model);

    Boolean findInList(String name, List<Pair<U, String>> fields, T model);

}
