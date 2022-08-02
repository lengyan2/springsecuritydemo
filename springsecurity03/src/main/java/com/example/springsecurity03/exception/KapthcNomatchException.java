package com.example.springsecurity03.exception;

import javax.naming.AuthenticationException;

public class KapthcNomatchException extends AuthenticationException {
    public KapthcNomatchException(String mes){
        super(mes);
    }
}
