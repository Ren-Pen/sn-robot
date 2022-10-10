package com.slimenano.sdk.robot.exception;

public class WrongPasswordException extends LoginFailedException{
    public WrongPasswordException(String message) {
        super(message);
    }
}
