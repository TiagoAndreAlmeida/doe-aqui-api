package br.com.doeaqui.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.doeaqui.category.CategoryEntity;
import br.com.doeaqui.category.dto.response.CategoryResponse;
import br.com.doeaqui.category.dto.response.CategorySummaryResponse;

class CategoryMapperTest {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Test
    @DisplayName("Deve mapear CategoryEntity para CategoryResponse com todos os campos incluindo auditoria")
    void shouldMapEntityToResponse() {
        CategoryEntity entity = new CategoryEntity(1L, "Eletrônicos", "eletronicos");
        
        CategoryResponse response = categoryMapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Eletrônicos");
        assertThat(response.slug()).isEqualTo("eletronicos");
        assertThat(response.createdAt()).isEqualTo(entity.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve mapear CategoryEntity para CategorySummaryResponse corretamente")
    void shouldMapEntityToSummaryResponse() {
        CategoryEntity entity = new CategoryEntity(2L, "Móveis", "moveis");

        CategorySummaryResponse response = categoryMapper.toSummaryResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Móveis");
        assertThat(response.slug()).isEqualTo("moveis");
    }

    @Test
    @DisplayName("Deve retornar null ao mapear entidade nula")
    void shouldReturnNullWhenEntityIsNull() {
        assertThat(categoryMapper.toResponse(null)).isNull();
        assertThat(categoryMapper.toSummaryResponse(null)).isNull();
    }
}
