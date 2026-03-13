package br.com.doeaqui.product.exception;

import org.springframework.http.HttpStatus;

import br.com.doeaqui.exception.BusinessException;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
