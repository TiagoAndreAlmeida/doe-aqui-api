package br.com.doeaqui.application.usecases.category;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

public class DeleteCategoryInteractor {
    private CategoryGateway categoryGateway;

    public DeleteCategoryInteractor(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    public void execute(String slug) {
        Category category = this.categoryGateway.findBySlug(slug)
        .orElseThrow(() -> new BusinessException("Categoria não encontrada", ErrorCode.NOT_FOUND));

        this.categoryGateway.deleteById(category.getId());
    }
}
