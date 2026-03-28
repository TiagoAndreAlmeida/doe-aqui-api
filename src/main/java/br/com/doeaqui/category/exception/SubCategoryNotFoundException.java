package br.com.doeaqui.category.exception;

import org.springframework.http.HttpStatus;

public class SubCategoryNotFoundException extends CategoryBusinessException {
    public SubCategoryNotFoundException(String slug) {
        super("Subcategoria não encontrada com o slug: " + slug, HttpStatus.NOT_FOUND);
    }
}
