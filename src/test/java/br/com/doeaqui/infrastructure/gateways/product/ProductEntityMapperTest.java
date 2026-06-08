package br.com.doeaqui.infrastructure.gateways.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.category.mapper.CategoryMapper;
import br.com.doeaqui.category.mapper.SubCategoryMapper;
import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.enums.ConditionStatus;
import br.com.doeaqui.domain.enums.DonationStatus;
import br.com.doeaqui.infrastructure.gateways.user.UserEntityMapper;
import br.com.doeaqui.infrastructure.persistence.product.ProductEntity;

@ExtendWith(MockitoExtension.class)
class ProductEntityMapperTest {

    private ProductEntityMapper productEntityMapper;

    @BeforeEach
    void setUp() {
        // Usamos instâncias reais dos Mappers dependentes
        productEntityMapper = new ProductEntityMapper(new UserEntityMapper(), new SubCategoryMapper(new CategoryMapper(), null));
    }

    @Test
    @DisplayName("Deve mapear Product de domínio para ProductEntity")
    void shouldMapProductDomainToEntity() {
        User donor = new User();
        donor.setId(1L);
        donor.setName("Doador");
        donor.setEmail("doador@test.com");
        
        Product product = new Product();
        product.setTitle("Titulo");
        product.setDescription("Desc");
        product.setCondition(ConditionStatus.NEW);
        product.setStatus(DonationStatus.AVAILABLE);
        product.setDonor(donor);

        ProductEntity entity = productEntityMapper.toEntity(product);

        assertNotNull(entity);
        assertEquals(product.getTitle(), entity.getTitle());
        assertEquals(product.getDonor().getId(), entity.getDonor().getId());
    }
}
