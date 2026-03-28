package br.com.doeaqui.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.doeaqui.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.category.dto.response.CategoryResponse;
import br.com.doeaqui.category.dto.response.CategorySummaryResponse;
import br.com.doeaqui.category.dto.response.SubCategorySummaryResponse;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.exception.GlobalExceptionHandler;

@WebMvcTest(CategoryController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private SubCategoryService subCategoryService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve permitir acesso público à listagem de categorias")
    void shouldAllowPublicAccessToCategoriesList() throws Exception {
        CategorySummaryResponse summary = new CategorySummaryResponse(1L, "Eletrônicos", "eletronicos", LocalDateTime.now());
        when(categoryService.findAll()).thenReturn(List.of(summary));

        mockMvc.perform(get("/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Eletrônicos"));
    }

    @Test
    @DisplayName("Deve permitir acesso público aos detalhes de uma categoria")
    void shouldAllowPublicAccessToCategoryDetails() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "Móveis", "moveis", LocalDateTime.now(), LocalDateTime.now());
        when(categoryService.findBySlug("moveis")).thenReturn(response);

        mockMvc.perform(get("/categories/moveis"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Móveis"));
    }

    @Test
    @DisplayName("Deve listar subcategorias de uma categoria de forma pública")
    void shouldAllowPublicAccessToSubCategoriesOfCategory() throws Exception {
        SubCategorySummaryResponse subSummary = new SubCategorySummaryResponse(10L, "Smartphones", "smartphones", LocalDateTime.now());
        when(subCategoryService.findByCategorySlug("eletronicos")).thenReturn(List.of(subSummary));

        mockMvc.perform(get("/categories/eletronicos/subcategories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Smartphones"));
    }

    @Test
    @DisplayName("Deve retornar 403 ao tentar criar categoria sem token")
    void shouldReturn403WhenCreatingCategoryWithoutToken() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Nova Categoria");

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve criar categoria com sucesso quando autenticado")
    void shouldCreateCategoryWhenAuthenticated() throws Exception {
        String token = "valid-token";
        CreateCategoryRequest request = new CreateCategoryRequest("Livros");
        CategoryResponse response = new CategoryResponse(1L, "Livros", "livros", LocalDateTime.now(), LocalDateTime.now());

        when(jwtService.getSubject(token)).thenReturn("admin@email.com");
        when(categoryService.create(any(CreateCategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/categories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Livros"));
    }

    @Test
    @DisplayName("Deve deletar categoria com sucesso quando autenticado")
    void shouldDeleteCategoryWhenAuthenticated() throws Exception {
        String token = "valid-token";
        when(jwtService.getSubject(token)).thenReturn("admin@email.com");

        mockMvc.perform(delete("/categories/eletronicos")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());
    }
}
