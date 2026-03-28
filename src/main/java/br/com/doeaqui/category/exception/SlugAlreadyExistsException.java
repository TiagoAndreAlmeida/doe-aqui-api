package br.com.doeaqui.category.exception;

import org.springframework.http.HttpStatus;

public class SlugAlreadyExistsException extends CategoryBusinessException {
    public SlugAlreadyExistsException(String slug) {
        super("Já existe uma categoria ou subcategoria com o slug: " + slug, HttpStatus.CONFLICT);
    }
}
