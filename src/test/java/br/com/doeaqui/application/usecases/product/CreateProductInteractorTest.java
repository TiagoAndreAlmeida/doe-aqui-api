package br.com.doeaqui.application.usecases.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import br.com.doeaqui.application.gateways.product.ProductGateway;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.Product;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.enums.ConditionStatus;
import br.com.doeaqui.domain.enums.DonationStatus;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateProductInteractorTest {

    @Mock
    private ProductGateway productGateway;

    @Mock
    private UserGateway userGateway;

    private CreateProductInteractor createProductInteractor;

    @BeforeEach
    void setUp() {
        createProductInteractor = new CreateProductInteractor(productGateway, userGateway);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso com status AVAILABLE")
    void shouldCreateProductSuccessfully() {
        // Arrange
        Long donorId = 1L;
        User donor = new User();
        donor.setId(donorId);
        
        Product inputProduct = new Product();
        inputProduct.setTitle("Produto Teste");
        inputProduct.setDescription("Descrição");
        inputProduct.setCondition(ConditionStatus.NEW);

        when(userGateway.findById(donorId)).thenReturn(Optional.of(donor));
        
        // Act
        createProductInteractor.createProduct(inputProduct, donorId);

        // Assert
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productGateway).createProduct(productCaptor.capture());
        
        Product savedProduct = productCaptor.getValue();
        assertEquals(DonationStatus.AVAILABLE, savedProduct.getStatus());
        assertEquals(donor, savedProduct.getDonor());
        assertEquals("Produto Teste", savedProduct.getTitle());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando doador não for encontrado")
    void shouldThrowExceptionWhenDonorNotFound() {
        // Arrange
        Long donorId = 999L;
        Product inputProduct = new Product();
        when(userGateway.findById(donorId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> createProductInteractor.createProduct(inputProduct, donorId));
        
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }
}
