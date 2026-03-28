package br.com.doeaqui.category.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotEmptyException extends CategoryBusinessException {
    public CategoryNotEmptyException(String name) {
        super("Não é possível excluir a categoria '" + name + "' pois ela contém subcategorias associadas.", HttpStatus.CONFLICT);
    }
}
