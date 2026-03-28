package br.com.doeaqui.category.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends CategoryBusinessException {
    public CategoryNotFoundException(String slug) {
        super("Categoria não encontrada com o slug: " + slug, HttpStatus.NOT_FOUND);
    }

    public CategoryNotFoundException(Long id) {
        super("Categoria não encontrada com o ID: " + id, HttpStatus.NOT_FOUND);
    }
}
