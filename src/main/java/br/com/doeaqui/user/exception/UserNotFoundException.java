package br.com.doeaqui.user.exception;

import org.springframework.http.HttpStatus;

import br.com.doeaqui.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
