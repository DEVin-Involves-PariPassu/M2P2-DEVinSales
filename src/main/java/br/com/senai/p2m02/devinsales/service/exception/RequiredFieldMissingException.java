package br.com.senai.p2m02.devinsales.service.exception;

public class RequiredFieldMissingException extends RuntimeException {
    public RequiredFieldMissingException(String message){
        super(message);
    }
}
