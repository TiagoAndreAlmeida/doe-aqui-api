package br.com.doeaqui.category.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import br.com.doeaqui.category.SubCategoryEntity;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;
import br.com.doeaqui.infrastructure.gateways.category.CategoryEntityMapper;
import br.com.doeaqui.infrastructure.gateways.product.ProductEntityMapper;

@Component
public class SubCategoryMapper {

    private final CategoryEntityMapper categoryMapper;
    private final ProductEntityMapper productEntityMapper;

    public SubCategoryMapper(CategoryEntityMapper categoryMapper, @Lazy ProductEntityMapper productEntityMapper) {
        this.categoryMapper = categoryMapper;
        this.productEntityMapper = productEntityMapper;
    }

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

    public br.com.doeaqui.category.SubCategoryEntity toEntity(br.com.doeaqui.domain.entity.Subcategory domain) {
        return new br.com.doeaqui.category.SubCategoryEntity(domain.getName(), domain.getSlug(), null);
    }

    public br.com.doeaqui.domain.entity.Subcategory toDomain(SubCategoryEntity entity) {
        return new br.com.doeaqui.domain.entity.Subcategory(
            entity.getId(),
            entity.getName(),
            entity.getSlug(),
            categoryMapper.toDomain(entity.getCategory()),
            entity.getProducts().stream().map(productEntityMapper::toDomain).toList(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
