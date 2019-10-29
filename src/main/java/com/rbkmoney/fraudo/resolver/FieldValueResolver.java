package com.rbkmoney.fraudo.resolver;


public interface FieldValueResolver<T> {

    String resolve(String fieldName, T model);

}
