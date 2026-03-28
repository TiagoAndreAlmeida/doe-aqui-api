package br.com.doeaqui.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSubCategoryRequest(
    @NotBlank(message = "O nome da subcategoria é obrigatório")
    @Size(max = 50, message = "O nome da subcategoria deve ter no máximo 50 caracteres")
    String name,

    @NotBlank(message = "O slug da categoria pai é obrigatório")
    String categorySlug
) {}
