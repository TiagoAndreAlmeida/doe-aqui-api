package br.com.doeaqui.application.usecases.category;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

public class FindCategoryBySlugInteractor {
    private CategoryGateway categoryGateway;

    public FindCategoryBySlugInteractor(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    public Category execute(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new BusinessException("O slug da categoria é obrigatório", ErrorCode.INVALID_STATE);
        }
        return this.categoryGateway.findBySlug(slug)
        .orElseThrow(() -> new BusinessException("Categoria não encontrada com o slug: "+slug, ErrorCode.NOT_FOUND));
    }
}
