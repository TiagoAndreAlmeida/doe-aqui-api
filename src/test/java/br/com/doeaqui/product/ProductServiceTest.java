package br.com.doeaqui.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.doeaqui.product.dto.request.CreateProductRequest;
import br.com.doeaqui.product.dto.response.ProductResponse;
import br.com.doeaqui.product.dto.response.ProductSummaryResponse;
import br.com.doeaqui.product.enums.ConditionStatus;
import br.com.doeaqui.product.enums.DonationStatus;
import br.com.doeaqui.product.exception.ProductBusinessException;
import br.com.doeaqui.product.exception.ProductNotFoundException;
import br.com.doeaqui.user.UserEntity;
import br.com.doeaqui.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void shouldCreateProductSuccessfully() {
        Long donorId = 1L;
        CreateProductRequest request = new CreateProductRequest("Cadeira", "Cadeira boa", ConditionStatus.USED);
        UserEntity donor = createUser(donorId, "Doador");
        
        when(userRepository.getReferenceById(donorId)).thenReturn(donor);
        ProductEntity savedEntity = new ProductEntity(10L, request.title(), request.description(), request.condition(), DonationStatus.AVAILABLE, donor);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedEntity);

        ProductResponse response = productService.create(request, donorId);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.status()).isEqualTo(DonationStatus.AVAILABLE);
        verify(userRepository).getReferenceById(donorId);
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Deve listar produtos disponíveis")
    void shouldListAvailableProducts() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        ProductEntity p1 = createProduct(1L, "P1", DonationStatus.AVAILABLE, createUser(1L, "U1"));
        Page<ProductEntity> page = new PageImpl<>(List.of(p1));

        when(productRepository.findAllByStatus(DonationStatus.AVAILABLE, pageRequest)).thenReturn(page);

        Page<ProductSummaryResponse> response = productService.listAvailable(pageRequest);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).title()).isEqualTo("P1");
    }

    @Test
    @DisplayName("Deve reservar um produto com sucesso")
    void shouldReserveProductSuccessfully() {
        Long productId = 10L;
        Long receiverId = 2L;
        UserEntity donor = createUser(1L, "Doador");
        UserEntity receiver = createUser(receiverId, "Recebedor");
        ProductEntity product = createProduct(productId, "Produto", DonationStatus.AVAILABLE, donor);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.getReferenceById(receiverId)).thenReturn(receiver);
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ProductResponse response = productService.reserve(productId, receiverId);

        assertThat(response.status()).isEqualTo(DonationStatus.RESERVED);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Não deve reservar produto se o recebedor for o doador")
    void shouldThrowExceptionWhenReceiverIsDonor() {
        Long productId = 10L;
        Long donorId = 1L;
        UserEntity donor = createUser(donorId, "Doador");
        ProductEntity product = createProduct(productId, "Produto", DonationStatus.AVAILABLE, donor);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.reserve(productId, donorId))
            .isInstanceOf(ProductBusinessException.class)
            .hasMessage("O doador não pode reservar o próprio produto");
    }

    @Test
    @DisplayName("Deve confirmar doação com sucesso")
    void shouldConfirmDonationSuccessfully() {
        Long productId = 10L;
        Long donorId = 1L;
        UserEntity donor = createUser(donorId, "Doador");
        ProductEntity product = createProduct(productId, "Produto", DonationStatus.RESERVED, donor);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ProductResponse response = productService.confirmDonation(productId, donorId);

        assertThat(response.status()).isEqualTo(DonationStatus.DONATED);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto inexistente")
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(1L))
            .isInstanceOf(ProductNotFoundException.class);
    }

    // Helpers
    private UserEntity createUser(Long id, String name) {
        return new UserEntity(id, name, name.toLowerCase() + "@email.com", "1199999", "pass", false);
    }

    private ProductEntity createProduct(Long id, String title, DonationStatus status, UserEntity donor) {
        return new ProductEntity(id, title, "Desc", ConditionStatus.USED, status, donor);
    }
}
