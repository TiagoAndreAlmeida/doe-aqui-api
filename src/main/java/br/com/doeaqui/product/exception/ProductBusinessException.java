package br.com.doeaqui.product.exception;

import org.springframework.http.HttpStatus;
import br.com.doeaqui.exception.BusinessException;

public class ProductBusinessException extends BusinessException {
    public ProductBusinessException(String message, HttpStatus status) {
        super(message, status);
    }
}
