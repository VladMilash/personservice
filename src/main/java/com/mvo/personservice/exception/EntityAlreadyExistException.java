package com.mvo.personservice.exception;

public class EntityAlreadyExistException extends ApiException {
    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
