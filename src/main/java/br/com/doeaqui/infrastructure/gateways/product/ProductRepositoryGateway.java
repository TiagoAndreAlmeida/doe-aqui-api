package br.com.doeaqui.infrastructure.gateways.product;

import br.com.doeaqui.application.gateways.product.ProductGateway;
import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.infrastructure.persistence.product.ProductEntity;
import br.com.doeaqui.infrastructure.persistence.product.ProductRepository;

public class ProductRepositoryGateway implements ProductGateway {
    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    public ProductRepositoryGateway(ProductRepository productRepository, ProductEntityMapper productEntityMapper) {
        this.productRepository = productRepository;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public Product createProduct(Product product) {
        ProductEntity entity = productEntityMapper.toEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return productEntityMapper.toDomain(savedEntity);
    }
}
