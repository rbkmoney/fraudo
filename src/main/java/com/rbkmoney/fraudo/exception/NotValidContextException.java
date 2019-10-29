package com.rbkmoney.fraudo.exception;

public class NotValidContextException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Context is not valid!";

    public NotValidContextException() {
        super(ERROR_MESSAGE);
    }
}
