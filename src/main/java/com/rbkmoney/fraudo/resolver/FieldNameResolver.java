package com.rbkmoney.fraudo.resolver;


public interface FieldNameResolver<T> {

    T resolve(String fieldName);

}
