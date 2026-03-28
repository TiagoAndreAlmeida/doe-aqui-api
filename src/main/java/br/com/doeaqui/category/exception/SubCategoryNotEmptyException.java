package br.com.doeaqui.category.exception;

import org.springframework.http.HttpStatus;

public class SubCategoryNotEmptyException extends CategoryBusinessException {
    public SubCategoryNotEmptyException(String name) {
        super("Não é possível excluir a subcategoria '" + name + "' pois ela contém produtos associados.", HttpStatus.CONFLICT);
    }
}
