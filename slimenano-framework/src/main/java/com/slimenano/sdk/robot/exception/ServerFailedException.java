package com.slimenano.sdk.robot.exception;

public class ServerFailedException extends LoginFailedException{
    public ServerFailedException(String message) {
        super(message);
    }
}
