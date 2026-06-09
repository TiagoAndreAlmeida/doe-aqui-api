package br.com.doeaqui.infrastructure.gateways.category;

import org.springframework.stereotype.Component;

import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.infrastructure.persistence.category.CategoryEntity;

@Component
public class CategoryEntityMapper {

    public CategoryEntity toEntity(Category categoryDomain) {
        return new CategoryEntity(
            categoryDomain.getId(),
            categoryDomain.getName(),
            categoryDomain.getSlug()
        );
    }

    public Category toDomain(CategoryEntity categoryEntity) {
        return new Category(
            categoryEntity.getId(),
            categoryEntity.getName(),
            categoryEntity.getSlug()
        );
    }

}
