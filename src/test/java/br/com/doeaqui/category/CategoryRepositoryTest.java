package br.com.doeaqui.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve persistir uma categoria com sucesso")
    void shouldPersistCategorySuccessfully() {
        CategoryEntity category = new CategoryEntity("Eletrônicos", "eletronicos");
        
        CategoryEntity savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
        assertThat(savedCategory.getName()).isEqualTo("Eletrônicos");
        assertThat(savedCategory.getSlug()).isEqualTo("eletronicos");
    }

    @Test
    @DisplayName("Deve encontrar uma categoria pelo slug")
    void shouldFindCategoryBySlug() {
        CategoryEntity category = new CategoryEntity("Móveis", "moveis");
        entityManager.persist(category);
        entityManager.flush();

        Optional<CategoryEntity> found = categoryRepository.findBySlug("moveis");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Móveis");
    }

    @Test
    @DisplayName("Deve preencher campos de auditoria automaticamente")
    void shouldPopulateAuditFieldsAutomatically() {
        CategoryEntity category = new CategoryEntity("Roupas", "roupas");
        
        CategoryEntity savedCategory = categoryRepository.save(category);
        entityManager.flush();

        assertThat(savedCategory.getCreatedAt()).isNotNull();
        assertThat(savedCategory.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Não deve permitir salvar categorias com o mesmo slug")
    void shouldNotAllowDuplicateSlugs() {
        CategoryEntity category1 = new CategoryEntity("Livros", "livros");
        entityManager.persist(category1);
        
        CategoryEntity category2 = new CategoryEntity("Outros Livros", "livros");
        
        assertThatThrownBy(() -> {
            categoryRepository.save(category2);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Não deve permitir salvar categoria sem nome")
    void shouldNotAllowSaveWithoutName() {
        CategoryEntity category = new CategoryEntity(null, "slug-sem-nome");

        assertThatThrownBy(() -> {
            categoryRepository.save(category);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Não deve permitir salvar categoria sem slug")
    void shouldNotAllowSaveWithoutSlug() {
        CategoryEntity category = new CategoryEntity("Nome Válido", null);

        assertThatThrownBy(() -> {
            categoryRepository.save(category);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Não deve permitir salvar nome com mais de 50 caracteres")
    void shouldNotAllowNameLongerThan50Characters() {
        String longName = "A".repeat(51);
        CategoryEntity category = new CategoryEntity(longName, "slug-valido");

        assertThatThrownBy(() -> {
            categoryRepository.save(category);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Não deve permitir salvar slug com mais de 50 caracteres")
    void shouldNotAllowSlugLongerThan50Characters() {
        String longSlug = "S".repeat(51);
        CategoryEntity category = new CategoryEntity("Nome Válido", longSlug);

        assertThatThrownBy(() -> {
            categoryRepository.save(category);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar slug inexistente")
    void shouldReturnEmptyWhenSlugDoesNotExist() {
        Optional<CategoryEntity> found = categoryRepository.findBySlug("slug-que-nao-existe");

        assertThat(found).isEmpty();
    }
}
