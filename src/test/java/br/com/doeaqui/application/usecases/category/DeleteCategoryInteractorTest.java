package br.com.doeaqui.application.usecases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
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
class DeleteCategoryInteractorTest {

    @Mock
    private CategoryGateway categoryGateway;

    private DeleteCategoryInteractor deleteCategoryInteractor;

    @BeforeEach
    void setUp() {
        deleteCategoryInteractor = new DeleteCategoryInteractor(categoryGateway);
    }

    @Test
    @DisplayName("Deve deletar categoria com sucesso quando o slug existir")
    void shouldDeleteCategorySuccessfully() {
        // Arrange
        String slug = "test-category";
        Category category = new Category();
        category.setId(1L);
        category.setSlug(slug);

        when(categoryGateway.findBySlug(slug)).thenReturn(Optional.of(category));

        // Act
        deleteCategoryInteractor.execute(slug);

        // Assert
        verify(categoryGateway).findBySlug(slug);
        verify(categoryGateway).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando tentar deletar categoria inexistente")
    void shouldThrowExceptionWhenDeletingInexistentCategory() {
        // Arrange
        String slug = "non-existent";
        when(categoryGateway.findBySlug(slug)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> deleteCategoryInteractor.execute(slug));
        
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
        verify(categoryGateway).findBySlug(slug);
        verify(categoryGateway, never()).deleteById(anyLong());
    }
}
