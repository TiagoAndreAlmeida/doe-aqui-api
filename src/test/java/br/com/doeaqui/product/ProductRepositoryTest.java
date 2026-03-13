package br.com.doeaqui.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.doeaqui.product.enums.ConditionStatus;
import br.com.doeaqui.product.enums.DonationStatus;
import br.com.doeaqui.user.UserEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve salvar um produto com sucesso associado a um doador")
    void shouldSaveProductWithDonor() {
        UserEntity donor = createAndPersistUser("Doador Teste", "doador@email.com");
        ProductEntity product = createProduct("Cadeira", ConditionStatus.USED, DonationStatus.AVAILABLE, donor);

        ProductEntity savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getDonor().getId()).isEqualTo(donor.getId());
    }

    @Test
    @DisplayName("Deve buscar produtos por status com paginação")
    void shouldFindProductsByStatusWithPagination() {
        UserEntity donor = createAndPersistUser("Doador 1", "d1@email.com");
        createAndPersistProduct("P1", DonationStatus.AVAILABLE, donor);
        createAndPersistProduct("P2", DonationStatus.AVAILABLE, donor);
        createAndPersistProduct("P3", DonationStatus.DONATED, donor);

        Page<ProductEntity> result = productRepository.findAllByStatus(DonationStatus.AVAILABLE, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting(ProductEntity::getTitle).containsExactlyInAnyOrder("P1", "P2");
    }

    @Test
    @DisplayName("Deve buscar produtos por ID do doador")
    void shouldFindProductsByDonorId() {
        UserEntity donor1 = createAndPersistUser("Doador 1", "d1@email.com");
        UserEntity donor2 = createAndPersistUser("Doador 2", "d2@email.com");
        
        createAndPersistProduct("D1-P1", DonationStatus.AVAILABLE, donor1);
        createAndPersistProduct("D2-P1", DonationStatus.AVAILABLE, donor2);

        Page<ProductEntity> result = productRepository.findAllByDonorId(donor1.getId(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("D1-P1");
    }

    @Test
    @DisplayName("Deve buscar produtos por ID do recebedor")
    void shouldFindProductsByReceiverId() {
        UserEntity donor = createAndPersistUser("Doador", "donor@email.com");
        UserEntity receiver = createAndPersistUser("Recebedor", "receiver@email.com");
        
        ProductEntity product = createProduct("Presente", ConditionStatus.NEW, DonationStatus.DONATED, donor);
        product.setReceiver(receiver);
        entityManager.persist(product);

        Page<ProductEntity> result = productRepository.findAllByReceiverId(receiver.getId(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getReceiver().getId()).isEqualTo(receiver.getId());
    }

    @Test
    @DisplayName("Não deve salvar um produto sem um doador")
    void shouldNotSaveProductWithoutDonor() {
        ProductEntity product = new ProductEntity(null, "Título", "Desc", ConditionStatus.NEW, DonationStatus.AVAILABLE, null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.save(product);
            productRepository.flush();
        });
    }

    // Métodos auxiliares
    private UserEntity createAndPersistUser(String name, String email) {
        UserEntity user = new UserEntity(null, name, email, "11999999999", "password", false);
        return entityManager.persist(user);
    }

    private ProductEntity createProduct(String title, ConditionStatus condition, DonationStatus status, UserEntity donor) {
        return new ProductEntity(null, title, "Descrição do produto", condition, status, donor);
    }

    private void createAndPersistProduct(String title, DonationStatus status, UserEntity donor) {
        entityManager.persist(createProduct(title, ConditionStatus.USED, status, donor));
    }
}
