package br.com.doeaqui.infrastructure.controllers.category;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.doeaqui.application.usecases.category.CreateCategoryInteractor;
import br.com.doeaqui.application.usecases.category.FindCategoryBySlugInteractor;
import br.com.doeaqui.application.usecases.category.ListCategoryInteractor;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.domain.entity.Category;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import br.com.doeaqui.exception.GlobalExceptionHandler;
import br.com.doeaqui.infrastructure.controllers.category.dto.CategoryDTOMapper;
import br.com.doeaqui.infrastructure.controllers.category.dto.request.CreateCategoryRequest;
import br.com.doeaqui.infrastructure.controllers.category.dto.response.CategoryResponse;
import br.com.doeaqui.infrastructure.controllers.category.dto.response.CategorySummaryResponse;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;

@WebMvcTest(CategoryController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateCategoryInteractor createCategoryInteractor;

    @MockitoBean
    private ListCategoryInteractor listCategoryInteractor;

    @MockitoBean
    private FindCategoryBySlugInteractor findCategoryBySlugInteractor;

    @MockitoBean
    private CategoryDTOMapper categoryDTOMapper;

    @MockitoBean
    private JwtService jwtService; // Necessário para o SecurityConfig

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve criar categoria com sucesso e retornar 201")
    void shouldCreateCategorySuccessfully() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Eletrônicos");
        Category domain = new Category();
        domain.setName("Eletrônicos");
        
        Category savedDomain = new Category();
        savedDomain.setId(1L);
        savedDomain.setName("Eletrônicos");
        savedDomain.setSlug("eletronicos");

        CategoryResponse response = new CategoryResponse(1L, "Eletrônicos", "eletronicos", LocalDateTime.now(), LocalDateTime.now());

        when(categoryDTOMapper.toDomain(any(CreateCategoryRequest.class))).thenReturn(domain);
        when(createCategoryInteractor.execute(domain)).thenReturn(savedDomain);
        when(categoryDTOMapper.toResponse(savedDomain)).thenReturn(response);

        UserEntity principal = new UserEntity(1L, "Admin", "admin@test.com", "pass", "123", false);

        mockMvc.perform(post("/categories")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Eletrônicos"))
            .andExpect(jsonPath("$.slug").value("eletronicos"));
    }

    @Test
    @DisplayName("Deve retornar lista de categorias e 200")
    void shouldReturnListOfCategories() throws Exception {
        Category cat1 = new Category();
        cat1.setName("Roupas");
        Category cat2 = new Category();
        cat2.setName("Móveis");
        List<Category> categories = Arrays.asList(cat1, cat2);

        CategorySummaryResponse res1 = new CategorySummaryResponse(1L, "Roupas", "roupas", LocalDateTime.now());
        CategorySummaryResponse res2 = new CategorySummaryResponse(2L, "Móveis", "moveis", LocalDateTime.now());

        when(listCategoryInteractor.execute()).thenReturn(categories);
        when(categoryDTOMapper.toSummaryResponse(cat1)).thenReturn(res1);
        when(categoryDTOMapper.toSummaryResponse(cat2)).thenReturn(res2);

        mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Roupas"))
            .andExpect(jsonPath("$[1].name").value("Móveis"));
    }

    @Test
    @DisplayName("Deve retornar 200 e a categoria quando buscar por slug existente")
    void shouldReturnOkAndCategoryWhenSlugExists() throws Exception {
        String slug = "eletronicos";
        Category domain = new Category();
        domain.setId(1L);
        domain.setName("Eletrônicos");
        domain.setSlug(slug);

        CategoryResponse response = new CategoryResponse(1L, "Eletrônicos", slug, LocalDateTime.now(), LocalDateTime.now());

        when(findCategoryBySlugInteractor.execute(slug)).thenReturn(domain);
        when(categoryDTOMapper.toResponse(domain)).thenReturn(response);

        mockMvc.perform(get("/categories/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.slug").value(slug))
            .andExpect(jsonPath("$.name").value("Eletrônicos"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando o slug da categoria não existir")
    void shouldReturnNotFoundWhenSlugDoesNotExist() throws Exception {
        String slug = "inexistente";
        String expectedMessage = "Categoria não encontrada com o slug: " + slug;

        when(findCategoryBySlugInteractor.execute(slug))
            .thenThrow(new BusinessException(expectedMessage, ErrorCode.NOT_FOUND));

        mockMvc.perform(get("/categories/{slug}", slug)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value(expectedMessage))
            .andExpect(jsonPath("$.path").value("/categories/" + slug))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve retornar 400 quando o nome da categoria for inválido")
    void shouldReturn400WhenNameIsInvalid() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest(""); // Nome vazio
        UserEntity principal = new UserEntity(1L, "Admin", "admin@test.com", "pass", "123", false);

        mockMvc.perform(post("/categories")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("Erro de validação")))
            .andExpect(jsonPath("$.message").value(containsString("nome")));
    }

    @Test
    @DisplayName("Deve retornar 409 quando tentar criar categoria com slug já existente")
    void shouldReturn409WhenSlugAlreadyExists() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Repetida");
        Category domain = new Category();
        domain.setName("Repetida");
        UserEntity principal = new UserEntity(1L, "Admin", "admin@test.com", "pass", "123", false);

        when(categoryDTOMapper.toDomain(any(CreateCategoryRequest.class))).thenReturn(domain);
        when(createCategoryInteractor.execute(domain))
            .thenThrow(new BusinessException("Já existe uma categoria com o slug: repetida", ErrorCode.ALREADY_EXISTS));

        mockMvc.perform(post("/categories")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value(containsString("repetida")));
    }
}
