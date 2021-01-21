package com.rbkmoney.fraudo.payment.resolver;

public interface CustomerTypeResolver<T> {

    Boolean isTrusted(T model);

}
