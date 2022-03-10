package br.com.senai.p2m02.devinsales.service.exception;

public class UserIsUnderAgeException extends RuntimeException {
    public UserIsUnderAgeException (String message) {
        super(message);
    }
}
