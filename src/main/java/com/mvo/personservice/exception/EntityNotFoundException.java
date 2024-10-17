package com.mvo.personservice.exception;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
