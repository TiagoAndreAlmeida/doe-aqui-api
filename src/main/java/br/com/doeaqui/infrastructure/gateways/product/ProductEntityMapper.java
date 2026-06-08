package br.com.doeaqui.infrastructure.gateways.product;

import org.springframework.stereotype.Component;
import br.com.doeaqui.category.mapper.SubCategoryMapper;
import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.infrastructure.gateways.user.UserEntityMapper;
import br.com.doeaqui.infrastructure.persistence.product.ProductEntity;

@Component
public class ProductEntityMapper {
    private final UserEntityMapper userEntityMapper;
    private final SubCategoryMapper subCategoryMapper;

    public ProductEntityMapper(UserEntityMapper userEntityMapper, SubCategoryMapper subCategoryMapper) {
        this.userEntityMapper = userEntityMapper;
        this.subCategoryMapper = subCategoryMapper;
    }

    public ProductEntity toEntity(Product productDomain) {
        ProductEntity entity = new ProductEntity(
            productDomain.getId(),
            productDomain.getTitle(),
            productDomain.getDescription(),
            productDomain.getCondition(),
            productDomain.getStatus(),
            userEntityMapper.toEntity(productDomain.getDonor())
        );
        
        if (productDomain.getReceiver() != null) {
            entity.setReceiver(userEntityMapper.toEntity(productDomain.getReceiver()));
        }
        
        if (productDomain.getSubcategory() != null) {
            entity.setSubcategory(subCategoryMapper.toEntity(productDomain.getSubcategory()));
        }
        
        return entity;
    }

    public Product toDomain(ProductEntity productEntity) {
        return new Product(
            productEntity.getId(),
            productEntity.getTitle(),
            productEntity.getDescription(),
            productEntity.getCondition(),
            productEntity.getStatus(),
            userEntityMapper.toDomain(productEntity.getDonor()),
            productEntity.getReceiver() != null ? userEntityMapper.toDomain(productEntity.getReceiver()) : null,
            subCategoryMapper.toDomain(productEntity.getSubcategory()),
            productEntity.getCreatedAt(),
            productEntity.getUpdatedAt()
        );
    }
}
