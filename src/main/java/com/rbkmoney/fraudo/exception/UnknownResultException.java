package com.rbkmoney.fraudo.exception;

public class UnknownResultException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Unknown result: ";

    public UnknownResultException(String result) {
        super(ERROR_MESSAGE + result);
    }

}
