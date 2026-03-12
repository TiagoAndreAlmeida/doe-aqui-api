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
        // Cenário: Criar e persistir um doador primeiro
        UserEntity donor = createAndPersistUser("Doador Teste", "doador@email.com");

        ProductEntity product = new ProductEntity(
            null, 
            "Cadeira de Escritório", 
            "Cadeira ergonômica em bom estado", 
            ConditionStatus.USED, 
            DonationStatus.AVAILABLE, 
            donor
        );

        // Ação
        ProductEntity savedProduct = productRepository.save(product);

        // Verificação
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getDonor().getId()).isEqualTo(donor.getId());
        assertThat(savedProduct.getCreatedAt()).isNotNull();
        assertThat(savedProduct.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Não deve salvar um produto sem um doador")
    void shouldNotSaveProductWithoutDonor() {
        ProductEntity product = new ProductEntity(
            null, 
            "Título sem doador", 
            "Descrição", 
            ConditionStatus.NEW, 
            DonationStatus.AVAILABLE, 
            null
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.save(product);
            productRepository.flush();
        });
    }

    @Test
    @DisplayName("Deve atualizar o updatedAt ao modificar um produto")
    void shouldUpdateTimestampOnModify() throws InterruptedException {
        UserEntity donor = createAndPersistUser("Doador Audit", "audit@email.com");
        ProductEntity product = new ProductEntity(
            null, "Produto Original", "Desc", 
            ConditionStatus.NEW, DonationStatus.AVAILABLE, donor
        );
        
        ProductEntity savedProduct = productRepository.saveAndFlush(product);
        var originalUpdatedAt = savedProduct.getUpdatedAt();

        // Simula uma pequena pausa para garantir que o timestamp mude
        Thread.sleep(100);

        savedProduct.setStatus(DonationStatus.RESERVED);
        ProductEntity updatedProduct = productRepository.saveAndFlush(savedProduct);

        assertThat(updatedProduct.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    // Método auxiliar para criar usuários nos testes
    private UserEntity createAndPersistUser(String name, String email) {
        UserEntity user = new UserEntity(null, name, email, "11999999999", "password", false);
        return entityManager.persist(user);
    }
}
