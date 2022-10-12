package com.slimenano.nscan.robot.exception;

public class WrongPasswordException extends LoginFailedException{
    public WrongPasswordException(String message) {
        super(message);
    }
}
