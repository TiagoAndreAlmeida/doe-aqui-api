package br.com.doeaqui.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.doeaqui.category.dto.request.CreateSubCategoryRequest;
import br.com.doeaqui.category.dto.response.SubCategoryResponse;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.exception.GlobalExceptionHandler;

@WebMvcTest(SubCategoryController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class SubCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubCategoryService subCategoryService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve criar uma subcategoria com sucesso quando autenticado")
    void shouldCreateSubCategoryWhenAuthenticated() throws Exception {
        String token = "valid-token";
        CreateSubCategoryRequest request = new CreateSubCategoryRequest("Smartphones", "eletronicos");
        SubCategoryResponse response = new SubCategoryResponse(1L, "Smartphones", "smartphones", "Eletrônicos", "eletronicos", LocalDateTime.now(), LocalDateTime.now());

        when(jwtService.getSubject(token)).thenReturn("admin@email.com");
        when(subCategoryService.create(any(CreateSubCategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/subcategories")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Smartphones"))
            .andExpect(jsonPath("$.categorySlug").value("eletronicos"));
    }

    @Test
    @DisplayName("Deve permitir acesso público aos detalhes de uma subcategoria")
    void shouldAllowPublicAccessToSubCategoryDetails() throws Exception {
        SubCategoryResponse response = new SubCategoryResponse(1L, "Smartphones", "smartphones", "Eletrônicos", "eletronicos", LocalDateTime.now(), LocalDateTime.now());
        when(subCategoryService.findBySlug("smartphones")).thenReturn(response);

        mockMvc.perform(get("/subcategories/smartphones"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Smartphones"));
    }

    @Test
    @DisplayName("Deve retornar 403 ao tentar criar subcategoria sem token")
    void shouldReturn403WhenCreatingWithoutToken() throws Exception {
        CreateSubCategoryRequest request = new CreateSubCategoryRequest("Smartphones", "eletronicos");

        mockMvc.perform(post("/subcategories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve deletar subcategoria com sucesso quando autenticado")
    void shouldDeleteSubCategoryWhenAuthenticated() throws Exception {
        String token = "valid-token";
        when(jwtService.getSubject(token)).thenReturn("admin@email.com");

        mockMvc.perform(delete("/subcategories/smartphones")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());
    }
}
