package br.com.doeaqui.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.exception.GlobalExceptionHandler;
import br.com.doeaqui.user.dto.request.LoginRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.user.dto.response.LoginResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve retornar 200 quando as credenciais forem enviadas corretamente")
    void shouldReturn200WhenCredentialsAreCorrect() throws Exception {
        LoginRequest request = new LoginRequest("exist@email.com", "123456");
        LoginResponse response = new LoginResponse("mocked-jwt-token");

        when(userService.authenticate(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
            .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    @DisplayName("Deve retornar 401 quando as credenciais forem inválidas")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        LoginRequest request = new LoginRequest("wrong@email.com", "wrongpass");

        when(userService.authenticate(any(LoginRequest.class)))
            .thenThrow(new BadCredentialsException("E-mail ou senha inválidos"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 403 ao acessar rota protegida sem token")
    void shouldReturn403WhenAccessingProtectedRouteWithoutToken() throws Exception {
        mockMvc.perform(get("/auth/me"))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 ao acessar /me com token válido")
    void shouldReturn200WhenAccessingMeWithValidToken() throws Exception {
        String token = "valid-token";
        String email = "user@email.com";
        UserEntity user = new UserEntity(1L, "User", email, "", "hash", false);

        when(jwtService.getSubject(token)).thenReturn(email);
        when(userService.findByEmail(email)).thenReturn(user);

        mockMvc.perform(get("/auth/me")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.name").value("User"));
    }
}
