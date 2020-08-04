package com.rbkmoney.fraudo.payment.resolver;

public interface PaymentTypeResolver<T> {

    Boolean isMobile(T model);

    Boolean isRecurrent(T model);

}
