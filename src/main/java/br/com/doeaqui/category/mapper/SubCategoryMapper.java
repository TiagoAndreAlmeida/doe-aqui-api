package br.com.doeaqui.category.mapper;

import org.springframework.stereotype.Component;

import br.com.doeaqui.category.SubCategoryEntity;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;

@Component
public class SubCategoryMapper {

    public SubCategoryResponse toResponse(SubCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new SubCategoryResponse(
            entity.getId(),
            entity.getName(),
            entity.getSlug(),
            entity.getCategory().getName(),
            entity.getCategory().getSlug(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public SubCategorySummaryResponse toSummaryResponse(SubCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new SubCategorySummaryResponse(
            entity.getId(),
            entity.getName(),
            entity.getSlug(),
            entity.getCreatedAt()
        );
    }
}
