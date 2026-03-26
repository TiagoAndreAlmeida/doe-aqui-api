package br.com.doeaqui.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubCategoryRepositoryTest {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    private CategoryEntity savedCategory;

    @BeforeEach
    void setUp() {
        CategoryEntity category = new CategoryEntity("Eletrônicos", "eletronicos");
        savedCategory = entityManager.persist(category);
        entityManager.flush();
    }

    @Test
    @DisplayName("Deve persistir uma subcategoria com sucesso")
    void shouldPersistSubCategorySuccessfully() {
        SubCategoryEntity subCategory = new SubCategoryEntity("Smartphones", "smartphones", savedCategory);
        
        SubCategoryEntity savedSubCategory = subCategoryRepository.save(subCategory);

        assertThat(savedSubCategory.getId()).isGreaterThan(0);
        assertThat(savedSubCategory.getName()).isEqualTo("Smartphones");
        assertThat(savedSubCategory.getCategory().getName()).isEqualTo("Eletrônicos");
    }

    @Test
    @DisplayName("Deve encontrar uma subcategoria pelo slug")
    void shouldFindSubCategoryBySlug() {
        SubCategoryEntity subCategory = new SubCategoryEntity("Notebooks", "notebooks", savedCategory);
        entityManager.persist(subCategory);
        entityManager.flush();

        Optional<SubCategoryEntity> found = subCategoryRepository.findBySlug("notebooks");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Notebooks");
    }

    @Test
    @DisplayName("Deve encontrar subcategorias pelo slug da categoria pai")
    void shouldFindSubCategoriesByCategorySlug() {
        SubCategoryEntity sub1 = new SubCategoryEntity("Sub 1", "sub-1", savedCategory);
        SubCategoryEntity sub2 = new SubCategoryEntity("Sub 2", "sub-2", savedCategory);
        entityManager.persist(sub1);
        entityManager.persist(sub2);
        entityManager.flush();

        List<SubCategoryEntity> found = subCategoryRepository.findByCategorySlug("eletronicos");

        assertThat(found).hasSize(2);
        assertThat(found).extracting(SubCategoryEntity::getSlug).containsExactlyInAnyOrder("sub-1", "sub-2");
    }

    @Test
    @DisplayName("Não deve permitir salvar subcategorias com o mesmo slug")
    void shouldNotAllowDuplicateSlugs() {
        SubCategoryEntity sub1 = new SubCategoryEntity("Sub 1", "mesmo-slug", savedCategory);
        entityManager.persist(sub1);
        
        SubCategoryEntity sub2 = new SubCategoryEntity("Sub 2", "mesmo-slug", savedCategory);
        
        assertThatThrownBy(() -> {
            subCategoryRepository.save(sub2);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Não deve permitir salvar subcategoria sem categoria pai")
    void shouldNotAllowSaveWithoutCategory() {
        SubCategoryEntity subCategory = new SubCategoryEntity("Sub sem pai", "sub-sem-pai", null);

        assertThatThrownBy(() -> {
            subCategoryRepository.save(subCategory);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Não deve permitir salvar nome com mais de 50 caracteres")
    void shouldNotAllowNameLongerThan50Characters() {
        String longName = "S".repeat(51);
        SubCategoryEntity subCategory = new SubCategoryEntity(longName, "slug-valido", savedCategory);

        assertThatThrownBy(() -> {
            subCategoryRepository.save(subCategory);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Deve preencher campos de auditoria automaticamente")
    void shouldPopulateAuditFieldsAutomatically() {
        SubCategoryEntity subCategory = new SubCategoryEntity("Audit", "audit", savedCategory);
        
        SubCategoryEntity saved = subCategoryRepository.save(subCategory);
        entityManager.flush();

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
}
