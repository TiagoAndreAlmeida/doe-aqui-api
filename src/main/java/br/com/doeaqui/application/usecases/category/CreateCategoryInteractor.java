package br.com.doeaqui.application.usecases.category;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import br.com.doeaqui.domain.service.SlugGenerator;

public class CreateCategoryInteractor {
    private SlugGenerator slugGenerator;
    private CategoryGateway categoryGateway;

    public CreateCategoryInteractor(SlugGenerator slugGenerator, CategoryGateway categoryGateway) {
        this.slugGenerator = slugGenerator;
        this.categoryGateway = categoryGateway;
    }

    public Category execute(Category category) {
        String slug = slugGenerator.generate(category.getName());

        if (categoryGateway.existsBySlug(slug)) {
            throw new BusinessException("Já existe uma categoria com o slug: "+slug, ErrorCode.ALREADY_EXISTS);
        }

        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setSlug(slug);

        return this.categoryGateway.createCategory(newCategory);
    }
}
