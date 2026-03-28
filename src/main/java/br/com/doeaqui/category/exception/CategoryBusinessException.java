package br.com.doeaqui.category.exception;

import br.com.doeaqui.exception.BusinessException;
import org.springframework.http.HttpStatus;

public abstract class CategoryBusinessException extends BusinessException {
    protected CategoryBusinessException(String message, HttpStatus status) {
        super(message, status);
    }
}
