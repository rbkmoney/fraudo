package com.rbkmoney.fraudo.exception;

public class NotImplementedOperatorException extends RuntimeException{

    public static final String ERROR_MESSAGE = "not implemented: operator ";

    public NotImplementedOperatorException(String operator) {
        super(ERROR_MESSAGE + operator);
    }

}
