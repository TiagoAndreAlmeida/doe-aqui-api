package br.com.doeaqui.application.usecases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

@ExtendWith(MockitoExtension.class)
class FindCategoryBySlugInteractorTest {

    @Mock
    private CategoryGateway categoryGateway;

    private FindCategoryBySlugInteractor findCategoryBySlugInteractor;

    @BeforeEach
    void setUp() {
        findCategoryBySlugInteractor = new FindCategoryBySlugInteractor(categoryGateway);
    }

    @Test
    @DisplayName("Deve retornar a categoria quando o slug existir")
    void shouldReturnCategoryWhenSlugExists() {
        // Arrange
        String slug = "test-category";
        Category expectedCategory = new Category();
        expectedCategory.setSlug(slug);
        expectedCategory.setName("Test Category");

        when(categoryGateway.findBySlug(slug)).thenReturn(Optional.of(expectedCategory));

        // Act
        Category result = findCategoryBySlugInteractor.execute(slug);

        // Assert
        assertEquals(expectedCategory, result);
        verify(categoryGateway).findBySlug(slug);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando o slug não for encontrado")
    void shouldThrowExceptionWhenSlugNotFound() {
        // Arrange
        String slug = "non-existent";
        when(categoryGateway.findBySlug(slug)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> findCategoryBySlugInteractor.execute(slug));
        
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
        assertEquals("Categoria não encontrada com o slug: " + slug, exception.getMessage());
        verify(categoryGateway).findBySlug(slug);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando o slug for nulo ou vazio")
    void shouldThrowExceptionWhenSlugIsNullOrEmpty() {
        assertThrows(BusinessException.class, () -> findCategoryBySlugInteractor.execute(null));
        assertThrows(BusinessException.class, () -> findCategoryBySlugInteractor.execute(""));
        assertThrows(BusinessException.class, () -> findCategoryBySlugInteractor.execute("   "));
    }
}
