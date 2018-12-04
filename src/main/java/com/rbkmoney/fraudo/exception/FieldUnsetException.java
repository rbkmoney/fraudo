package com.rbkmoney.fraudo.exception;

public class FieldUnsetException extends RuntimeException {

    public static final String ERROR_MESSAGE = "Count target field is not set or bad format! (must be \"*\")";

    public FieldUnsetException() {
        super(ERROR_MESSAGE);
    }
}
