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

import br.com.doeaqui.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.category.dto.response.CategoryResponse;
import br.com.doeaqui.category.dto.response.CategorySummaryResponse;
import br.com.doeaqui.category.exception.CategoryNotEmptyException;
import br.com.doeaqui.category.exception.CategoryNotFoundException;
import br.com.doeaqui.category.exception.SlugAlreadyExistsException;
import br.com.doeaqui.category.mapper.CategoryMapper;
import br.com.doeaqui.util.SlugGenerator;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private SlugGenerator slugGenerator;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("Deve criar uma categoria com sucesso gerando o slug")
    void shouldCreateCategorySuccessfully() {
        // Arrange (Configuração)
        CreateCategoryRequest request = new CreateCategoryRequest("Eletrônicos");
        String generatedSlug = "eletronicos";
        CategoryEntity savedCategory = new CategoryEntity(1L, "Eletrônicos", generatedSlug);
        CategoryResponse expectedResponse = new CategoryResponse(1L, "Eletrônicos", generatedSlug, LocalDateTime.now(), LocalDateTime.now());

        when(slugGenerator.generate("Eletrônicos")).thenReturn(generatedSlug);
        when(categoryRepository.existsBySlug(generatedSlug)).thenReturn(false);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedCategory);
        when(categoryMapper.toResponse(savedCategory)).thenReturn(expectedResponse);

        // Act (Execução)
        CategoryResponse result = categoryService.create(request);

        // Assert (Validação)
        assertThat(result).isNotNull();
        assertThat(result.slug()).isEqualTo(generatedSlug);
        verify(categoryRepository).save(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar categoria com slug duplicado")
    void shouldThrowExceptionWhenSlugAlreadyExists() {
        // Arrange
        CreateCategoryRequest request = new CreateCategoryRequest("Eletrônicos");
        String generatedSlug = "eletronicos";

        when(slugGenerator.generate("Eletrônicos")).thenReturn(generatedSlug);
        when(categoryRepository.existsBySlug(generatedSlug)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> categoryService.create(request))
            .isInstanceOf(SlugAlreadyExistsException.class);
        
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar lista de resumos de categorias")
    void shouldReturnAllCategories() {
        // Arrange
        CategoryEntity cat1 = new CategoryEntity(1L, "Cat 1", "cat-1");
        CategorySummaryResponse summary1 = new CategorySummaryResponse(1L, "Cat 1", "cat-1", LocalDateTime.now());
        
        when(categoryRepository.findAll()).thenReturn(List.of(cat1));
        when(categoryMapper.toSummaryResponse(cat1)).thenReturn(summary1);

        // Act
        List<CategorySummaryResponse> result = categoryService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Cat 1");
    }

    @Test
    @DisplayName("Deve buscar categoria por slug com sucesso")
    void shouldFindCategoryBySlug() {
        // Arrange
        String slug = "eletronicos";
        CategoryEntity entity = new CategoryEntity(1L, "Eletrônicos", slug);
        CategoryResponse response = new CategoryResponse(1L, "Eletrônicos", slug, LocalDateTime.now(), LocalDateTime.now());

        when(categoryRepository.findBySlug(slug)).thenReturn(Optional.of(entity));
        when(categoryMapper.toResponse(entity)).thenReturn(response);

        // Act
        CategoryResponse result = categoryService.findBySlug(slug);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.slug()).isEqualTo(slug);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar slug inexistente")
    void shouldThrowNotFoundWhenSlugDoesNotExist() {
        when(categoryRepository.findBySlug("nao-existe")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findBySlug("nao-existe"))
            .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("Deve deletar categoria com sucesso")
    void shouldDeleteCategorySuccessfully() {
        // Arrange
        String slug = "vazia";
        CategoryEntity entity = new CategoryEntity(1L, "Vazia", slug);

        when(categoryRepository.findBySlug(slug)).thenReturn(Optional.of(entity));
        when(subCategoryRepository.findByCategorySlug(slug)).thenReturn(List.of());

        // Act
        categoryService.delete(slug);

        // Assert
        verify(categoryRepository).delete(entity);
    }

    @Test
    @DisplayName("Não deve deletar categoria que possui subcategorias")
    void shouldNotDeleteCategoryWithSubCategories() {
        // Arrange
        String slug = "com-filhos";
        CategoryEntity entity = new CategoryEntity(1L, "Com Filhos", slug);
        SubCategoryEntity subChild = new SubCategoryEntity(10L, "Filho", "filho", entity);

        when(categoryRepository.findBySlug(slug)).thenReturn(Optional.of(entity));
        when(subCategoryRepository.findByCategorySlug(slug)).thenReturn(List.of(subChild));

        // Act & Assert
        assertThatThrownBy(() -> categoryService.delete(slug))
            .isInstanceOf(CategoryNotEmptyException.class);
        
        verify(categoryRepository, never()).delete(any());
    }
}
