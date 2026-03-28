package br.com.doeaqui.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.category.dto.request.CreateSubCategoryRequest;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;
import br.com.doeaqui.category.exception.CategoryNotFoundException;
import br.com.doeaqui.category.exception.SubCategoryNotEmptyException;
import br.com.doeaqui.category.mapper.SubCategoryMapper;
import br.com.doeaqui.product.ProductRepository;
import br.com.doeaqui.util.SlugGenerator;

@ExtendWith(MockitoExtension.class)
class SubCategoryServiceTest {

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubCategoryMapper subCategoryMapper;

    @Mock
    private SlugGenerator slugGenerator;

    @InjectMocks
    private SubCategoryService subCategoryService;

    @Test
    @DisplayName("Deve criar uma subcategoria com sucesso")
    void shouldCreateSubCategorySuccessfully() {
        // Arrange
        CreateSubCategoryRequest request = new CreateSubCategoryRequest("Smartphones", "eletronicos");
        CategoryEntity parent = new CategoryEntity(1L, "Eletrônicos", "eletronicos");
        String subSlug = "smartphones";
        SubCategoryEntity savedEntity = new SubCategoryEntity(10L, "Smartphones", subSlug, parent);
        SubCategoryResponse response = new SubCategoryResponse(10L, "Smartphones", subSlug, "Eletrônicos", "eletronicos", LocalDateTime.now(), LocalDateTime.now());

        when(categoryRepository.findBySlug("eletronicos")).thenReturn(Optional.of(parent));
        when(slugGenerator.generate("Smartphones")).thenReturn(subSlug);
        when(subCategoryRepository.existsBySlug(subSlug)).thenReturn(false);
        when(subCategoryRepository.save(any(SubCategoryEntity.class))).thenReturn(savedEntity);
        when(subCategoryMapper.toResponse(savedEntity)).thenReturn(response);

        // Act
        SubCategoryResponse result = subCategoryService.create(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.categorySlug()).isEqualTo("eletronicos");
        verify(subCategoryRepository).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar subcategoria para categoria pai inexistente")
    void shouldThrowExceptionWhenParentCategoryNotFound() {
        CreateSubCategoryRequest request = new CreateSubCategoryRequest("Smartphones", "nao-existe");
        when(categoryRepository.findBySlug("nao-existe")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subCategoryService.create(request))
            .isInstanceOf(CategoryNotFoundException.class);
        
        verify(subCategoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar subcategorias por slug da categoria pai")
    void shouldFindSubCategoriesByCategorySlug() {
        // Arrange
        String catSlug = "moveis";
        SubCategoryEntity sub1 = new SubCategoryEntity(1L, "Cadeiras", "cadeiras", null);
        SubCategorySummaryResponse summary1 = new SubCategorySummaryResponse(1L, "Cadeiras", "cadeiras", LocalDateTime.now());

        when(categoryRepository.existsBySlug(catSlug)).thenReturn(true);
        when(subCategoryRepository.findByCategorySlug(catSlug)).thenReturn(List.of(sub1));
        when(subCategoryMapper.toSummaryResponse(sub1)).thenReturn(summary1);

        // Act
        List<SubCategorySummaryResponse> result = subCategoryService.findByCategorySlug(catSlug);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).slug()).isEqualTo("cadeiras");
    }

    @Test
    @DisplayName("Deve lançar erro ao listar subcategorias de uma categoria que não existe")
    void shouldThrowErrorWhenListingFromInexistentCategory() {
        when(categoryRepository.existsBySlug("fantasma")).thenReturn(false);

        assertThatThrownBy(() -> subCategoryService.findByCategorySlug("fantasma"))
            .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Deve deletar subcategoria com sucesso")
    void shouldDeleteSubCategorySuccessfully() {
        // Arrange
        String slug = "vazia";
        SubCategoryEntity sub = new SubCategoryEntity(10L, "Vazia", slug, null);

        when(subCategoryRepository.findBySlug(slug)).thenReturn(Optional.of(sub));
        when(productRepository.existsBySubcategoryId(sub.getId())).thenReturn(false);

        // Act
        subCategoryService.delete(slug);

        // Assert
        verify(subCategoryRepository).delete(sub);
    }

    @Test
    @DisplayName("Não deve deletar subcategoria que possui produtos vinculados")
    void shouldNotDeleteSubCategoryWithProducts() {
        // Arrange
        String slug = "com-produtos";
        SubCategoryEntity sub = new SubCategoryEntity(10L, "Com Produtos", slug, null);

        when(subCategoryRepository.findBySlug(slug)).thenReturn(Optional.of(sub));
        when(productRepository.existsBySubcategoryId(sub.getId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> subCategoryService.delete(slug))
            .isInstanceOf(SubCategoryNotEmptyException.class);
        
        verify(subCategoryRepository, never()).delete(any());
    }
}
