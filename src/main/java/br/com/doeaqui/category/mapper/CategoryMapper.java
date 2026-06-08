package br.com.doeaqui.category.mapper;

import org.springframework.stereotype.Component;
import br.com.doeaqui.category.CategoryEntity;
import br.com.doeaqui.domain.entity.Category;

@Component
public class CategoryMapper {
    public Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return new Category(
            entity.getId(),
            entity.getName(),
            entity.getSlug()
        );
    }

    public br.com.doeaqui.category.dto.response.CategoryResponse toResponse(CategoryEntity entity) {
        if (entity == null) return null;
        return new br.com.doeaqui.category.dto.response.CategoryResponse(
            entity.getId(),
            entity.getName(),
            entity.getSlug(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public br.com.doeaqui.category.dto.response.CategorySummaryResponse toSummaryResponse(CategoryEntity entity) {
        if (entity == null) return null;
        return new br.com.doeaqui.category.dto.response.CategorySummaryResponse(
            entity.getId(),
            entity.getName(),
            entity.getSlug(),
            entity.getCreatedAt()
        );
    }
}
