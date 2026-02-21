package br.com.doeaqui.user.exception;

import org.springframework.http.HttpStatus;

import br.com.doeaqui.exception.BusinessException;

public class InactiveUserException extends BusinessException {
    public InactiveUserException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
