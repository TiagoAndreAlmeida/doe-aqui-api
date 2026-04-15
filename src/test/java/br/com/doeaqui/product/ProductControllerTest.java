package br.com.doeaqui.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.domain.enums.ConditionStatus;
import br.com.doeaqui.domain.enums.DonationStatus;
import br.com.doeaqui.exception.GlobalExceptionHandler;
import br.com.doeaqui.product.dto.request.CreateProductRequest;
import br.com.doeaqui.product.dto.response.ProductResponse;
import br.com.doeaqui.product.dto.response.ProductSummaryResponse;
import br.com.doeaqui.user.dto.response.UserSummaryResponse;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve permitir acesso público à listagem de produtos")
    void shouldAllowPublicAccessToProductList() throws Exception {
        ProductSummaryResponse summary = new ProductSummaryResponse(1L, "Produto", ConditionStatus.USED, DonationStatus.AVAILABLE);
        when(productService.listAvailable(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(summary)));

        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].title").value("Produto"));
    }

    @Test
    @DisplayName("Deve permitir acesso público aos detalhes de um produto")
    void shouldAllowPublicAccessToProductDetails() throws Exception {
        ProductResponse response = new ProductResponse(1L, "Título", "Desc", ConditionStatus.NEW, DonationStatus.AVAILABLE, new UserSummaryResponse("Doador", "119999"), LocalDateTime.now());
        when(productService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Título"));
    }

    @Test
    @DisplayName("Deve retornar 403 ao tentar criar produto sem token")
    void shouldReturn403WhenCreatingProductWithoutToken() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Título", "Desc", ConditionStatus.NEW);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve criar produto com sucesso quando autenticado")
    void shouldCreateProductWhenAuthenticated() throws Exception {
        String token = "valid-token";
        String email = "user@email.com";
        Long userId = 1L;
        CreateProductRequest request = new CreateProductRequest("Título", "Desc", ConditionStatus.NEW);
        ProductResponse response = new ProductResponse(10L, "Título", "Desc", ConditionStatus.NEW, DonationStatus.AVAILABLE, new UserSummaryResponse("Doador", "119999"), LocalDateTime.now());

        when(jwtService.getSubject(token)).thenReturn(email);
        when(jwtService.getUserId(token)).thenReturn(userId);
        when(productService.create(any(CreateProductRequest.class), anyLong())).thenReturn(response);

        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.title").value("Título"));
    }

    @Test
    @DisplayName("Deve reservar produto com sucesso quando autenticado")
    void shouldReserveProductWhenAuthenticated() throws Exception {
        String token = "valid-token";
        String email = "user@email.com";
        Long userId = 1L;
        ProductResponse response = new ProductResponse(10L, "Título", "Desc", ConditionStatus.NEW, DonationStatus.RESERVED, new UserSummaryResponse("Doador", "119999"), LocalDateTime.now());

        when(jwtService.getSubject(token)).thenReturn(email);
        when(jwtService.getUserId(token)).thenReturn(userId);
        when(productService.reserve(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(patch("/products/10/reserve")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("RESERVED"));
    }
}
