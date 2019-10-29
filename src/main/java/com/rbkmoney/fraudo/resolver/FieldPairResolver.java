package com.rbkmoney.fraudo.resolver;


import com.rbkmoney.fraudo.model.Pair;

public interface FieldPairResolver<T, U> {

    Pair<U, String> resolve(String fieldName, T model);

}
