package br.com.doeaqui.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.doeaqui.category.CategoryEntity;
import br.com.doeaqui.category.SubCategoryEntity;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;

class SubCategoryMapperTest {

    private final SubCategoryMapper subCategoryMapper = new SubCategoryMapper();

    @Test
    @DisplayName("Deve mapear SubCategoryEntity para SubCategoryResponse com dados da categoria pai")
    void shouldMapEntityToResponseWithParentData() {
        CategoryEntity parent = new CategoryEntity(1L, "Eletrônicos", "eletronicos");
        SubCategoryEntity entity = new SubCategoryEntity(10L, "Smartphones", "smartphones", parent);
        
        SubCategoryResponse response = subCategoryMapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Smartphones");
        assertThat(response.slug()).isEqualTo("smartphones");
        assertThat(response.categoryName()).isEqualTo("Eletrônicos");
        assertThat(response.categorySlug()).isEqualTo("eletronicos");
    }

    @Test
    @DisplayName("Deve mapear SubCategoryEntity para SubCategorySummaryResponse corretamente")
    void shouldMapEntityToSummaryResponse() {
        CategoryEntity parent = new CategoryEntity(1L, "Móveis", "moveis");
        SubCategoryEntity entity = new SubCategoryEntity(20L, "Cadeiras", "cadeiras", parent);

        SubCategorySummaryResponse response = subCategoryMapper.toSummaryResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(20L);
        assertThat(response.name()).isEqualTo("Cadeiras");
        assertThat(response.slug()).isEqualTo("cadeiras");
    }

    @Test
    @DisplayName("Deve retornar null ao mapear subcategoria nula")
    void shouldReturnNullWhenEntityIsNull() {
        assertThat(subCategoryMapper.toResponse(null)).isNull();
        assertThat(subCategoryMapper.toSummaryResponse(null)).isNull();
    }
}
