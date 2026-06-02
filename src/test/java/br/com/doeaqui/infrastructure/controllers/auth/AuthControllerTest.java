package br.com.doeaqui.infrastructure.controllers.auth;

import br.com.doeaqui.application.usecases.auth.AuthenticateUserInteractor;
import br.com.doeaqui.config.JwtAuthenticationFilter;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import br.com.doeaqui.exception.GlobalExceptionHandler;
import br.com.doeaqui.infrastructure.controllers.auth.dto.AuthUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticateUserInteractor authenticateUserInteractor;

    @MockitoBean
    private JwtService jwtService; // Necessário pois JwtAuthenticationFilter depende dele

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve retornar 200 e o token quando o login for bem-sucedido")
    void shouldReturnOkAndTokenOnSuccess() throws Exception {
        // Arrange
        AuthUserRequest request = new AuthUserRequest("test@example.com", "password123");
        String token = "valid.jwt.token";

        when(authenticateUserInteractor.authenticate(request.email(), request.password())).thenReturn(token);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    @DisplayName("Deve retornar 401 quando o interactor lançar erro de autenticação")
    void shouldReturnUnauthorizedWhenAuthenticationFails() throws Exception {
        // Arrange
        AuthUserRequest request = new AuthUserRequest("wrong@example.com", "wrong_pass");

        when(authenticateUserInteractor.authenticate(anyString(), anyString()))
                .thenThrow(new BusinessException("E-mail ou senha inválidos", ErrorCode.UNAUTHORIZED));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("E-mail ou senha inválidos"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.path").value("/auth/login"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o request for inválido (Bean Validation)")
    void shouldReturnBadRequestWhenInputIsInvalid() throws Exception {
        // Arrange (E-mail inválido e senha vazia)
        AuthUserRequest request = new AuthUserRequest("invalid-email", "");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("Erro de validação")))
                .andExpect(jsonPath("$.message").value(containsString("email")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve retornar 500 quando ocorrer um erro inesperado no sistema")
    void shouldReturnInternalServerErrorOnUnexpectedException() throws Exception {
        // Arrange
        AuthUserRequest request = new AuthUserRequest("test@example.com", "password123");

        when(authenticateUserInteractor.authenticate(anyString(), anyString()))
                .thenThrow(new RuntimeException("Erro de banco de dados inesperado"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno inesperado"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
