package br.com.doeaqui.user.exception;

import br.com.doeaqui.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
