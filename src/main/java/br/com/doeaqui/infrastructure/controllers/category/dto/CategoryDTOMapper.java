package br.com.doeaqui.infrastructure.controllers.category.dto;

import org.springframework.stereotype.Component;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.infrastructure.controllers.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.infrastructure.controllers.category.dto.response.CategoryResponse;
import br.com.doeaqui.infrastructure.controllers.category.dto.response.CategorySummaryResponse;

@Component
public class CategoryDTOMapper {

    public Category toDomain(CreateCategoryRequest request) {
        return new Category(0, request.name(), null); // Slugs are generated in the Interactor/Service
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getSlug(),
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }

    public CategorySummaryResponse toSummaryResponse(Category category) {
        return new CategorySummaryResponse(
            category.getId(),
            category.getName(),
            category.getSlug(),
            category.getCreatedAt()
        );
    }
}
