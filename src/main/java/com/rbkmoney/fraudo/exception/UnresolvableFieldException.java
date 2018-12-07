package com.rbkmoney.fraudo.exception;

public class UnresolvableFieldException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Can't find this field: ";

    public UnresolvableFieldException(String fieldName) {
        super(ERROR_MESSAGE + fieldName);
    }

}
