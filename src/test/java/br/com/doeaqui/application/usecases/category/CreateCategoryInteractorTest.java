package br.com.doeaqui.application.usecases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import br.com.doeaqui.domain.service.SlugGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCategoryInteractorTest {

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private SlugGenerator slugGenerator;

    private CreateCategoryInteractor createCategoryInteractor;

    @BeforeEach
    void setUp() {
        createCategoryInteractor = new CreateCategoryInteractor(slugGenerator, categoryGateway);
    }

    @Test
    @DisplayName("Deve criar categoria com sucesso")
    void shouldCreateCategorySuccessfully() {
        // Arrange
        Category category = new Category();
        category.setName("Eletrônicos");
        String slug = "eletronicos";

        when(slugGenerator.generate("Eletrônicos")).thenReturn(slug);
        when(categoryGateway.existsBySlug(slug)).thenReturn(false);
        when(categoryGateway.createCategory(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        createCategoryInteractor.execute(category);

        // Assert
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryGateway).createCategory(categoryCaptor.capture());
        
        Category savedCategory = categoryCaptor.getValue();
        assertEquals("Eletrônicos", savedCategory.getName());
        assertEquals(slug, savedCategory.getSlug());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando slug já existe")
    void shouldThrowExceptionWhenSlugAlreadyExists() {
        // Arrange
        Category category = new Category();
        category.setName("Eletrônicos");
        String slug = "eletronicos";

        when(slugGenerator.generate("Eletrônicos")).thenReturn(slug);
        when(categoryGateway.existsBySlug(slug)).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> createCategoryInteractor.execute(category));
        
        assertEquals(ErrorCode.ALREADY_EXISTS, exception.getErrorCode());
        verify(categoryGateway, never()).createCategory(any(Category.class));
    }
}
