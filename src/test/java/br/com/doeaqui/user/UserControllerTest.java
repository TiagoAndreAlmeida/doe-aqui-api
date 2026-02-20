package br.com.doeaqui.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.exception.GlobalExceptionHandler;
import br.com.doeaqui.user.dto.request.CreateUserRequest;
import br.com.doeaqui.user.exception.EmailAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve retornar 201 ao criar usuário com sucesso")
    void shouldReturn201WhenCreateUserIsSuccessful() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Tiago", "tiago@email.com", "11999999999", "senha123");
        UserEntity savedUser = new UserEntity(1L, "Tiago", "tiago@email.com", "11999999999", "senha123", false);

        when(userService.create(any(CreateUserRequest.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Tiago"))
            .andExpect(jsonPath("$.email").value("tiago@email.com"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando os dados de entrada forem inválidos")
    void shouldReturn400WhenInputIsInvalid() throws Exception {
        CreateUserRequest request = new CreateUserRequest("", "email-invalido", "", "123");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.message").value(containsString("Erro de validação")));
    }

    @Test
    @DisplayName("Deve retornar 409 quando o e-mail já existir")
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Tiago", "duplicado@email.com", "", "senha123");

        when(userService.create(any(CreateUserRequest.class)))
            .thenThrow(new EmailAlreadyExistsException("E-mail já cadastrado"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value("E-mail já cadastrado"));
    }
}
