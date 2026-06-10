package br.com.doeaqui.application.usecases.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.application.gateways.category.CategoryGateway;
import br.com.doeaqui.domain.entity.Category;

@ExtendWith(MockitoExtension.class)
class ListCategoryInteractorTest {

    @Mock
    private CategoryGateway categoryGateway;

    private ListCategoryInteractor listCategoryInteractor;

    @BeforeEach
    void setUp() {
        listCategoryInteractor = new ListCategoryInteractor(categoryGateway);
    }

    @Test
    @DisplayName("Deve retornar uma lista de categorias quando existirem registros")
    void shouldReturnListOfCategories() {
        // Arrange
        Category cat1 = new Category();
        cat1.setName("Roupas");
        Category cat2 = new Category();
        cat2.setName("Móveis");
        
        List<Category> expectedList = Arrays.asList(cat1, cat2);
        when(categoryGateway.findAll()).thenReturn(expectedList);

        // Act
        List<Category> result = listCategoryInteractor.execute();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Roupas", result.get(0).getName());
        assertEquals("Móveis", result.get(1).getName());
        verify(categoryGateway).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver categorias")
    void shouldReturnEmptyListWhenNoCategoriesExist() {
        // Arrange
        when(categoryGateway.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Category> result = listCategoryInteractor.execute();

        // Assert
        assertTrue(result.isEmpty());
        verify(categoryGateway).findAll();
    }
}
