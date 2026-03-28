package br.com.doeaqui.category.mapper;

import org.springframework.stereotype.Component;

import br.com.doeaqui.category.CategoryEntity;
import br.com.doeaqui.category.dto.response.CategoryResponse;
import br.com.doeaqui.category.dto.response.CategorySummaryResponse;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new CategoryResponse(
            entity.getId(),
            entity.getName(),
            entity.getSlug(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public CategorySummaryResponse toSummaryResponse(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new CategorySummaryResponse(
            entity.getId(),
            entity.getName(),
            entity.getSlug(),
            entity.getCreatedAt()
        );
    }
}
