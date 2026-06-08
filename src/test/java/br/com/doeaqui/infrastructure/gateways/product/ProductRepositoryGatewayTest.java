package br.com.doeaqui.infrastructure.gateways.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.infrastructure.persistence.product.ProductEntity;
import br.com.doeaqui.infrastructure.persistence.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryGatewayTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductEntityMapper productEntityMapper;

    private ProductRepositoryGateway productRepositoryGateway;

    @BeforeEach
    void setUp() {
        productRepositoryGateway = new ProductRepositoryGateway(productRepository, productEntityMapper);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void shouldCreateProduct() {
        Product domain = new Product();
        domain.setTitle("Produto");
        
        ProductEntity entity = new ProductEntity();
        
        when(productEntityMapper.toEntity(domain)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);
        when(productEntityMapper.toDomain(entity)).thenReturn(domain);

        Product result = productRepositoryGateway.createProduct(domain);

        assertEquals(domain, result);
        verify(productRepository).save(entity);
    }
}
