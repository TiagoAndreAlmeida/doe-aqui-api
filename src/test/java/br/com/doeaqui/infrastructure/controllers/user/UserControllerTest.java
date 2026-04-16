package br.com.doeaqui.infrastructure.controllers.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

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

import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.exception.GlobalExceptionHandler;
import br.com.doeaqui.application.usecases.user.CreateUserInteractor;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.controllers.user.dto.CreateUserRequest;
import br.com.doeaqui.infrastructure.controllers.user.dto.CreateUserResponse;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserDTOMapper;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserInteractor createUserInteractor;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should return 201 when user creation is successful")
    void shouldReturn201WhenCreateUserIsSuccessful() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Tiago", "tiago@email.com", "11999999999", "senha123");
        User userDomain = new User();
        userDomain.setName("Tiago");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Tiago");
        savedUser.setEmail("tiago@email.com");
        savedUser.setInactive(false);
        savedUser.setCreatedAt(LocalDateTime.now());
        savedUser.setUpdatedAt(LocalDateTime.now());

        CreateUserResponse response = new CreateUserResponse(1L, "Tiago", "tiago@email.com", "11999999999", false, savedUser.getCreatedAt(), savedUser.getUpdatedAt());

        when(userDTOMapper.toDomain(any(CreateUserRequest.class))).thenReturn(userDomain);
        when(createUserInteractor.createUser(userDomain)).thenReturn(savedUser);
        when(userDTOMapper.toResponse(savedUser)).thenReturn(response);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Tiago"))
            .andExpect(jsonPath("$.email").value("tiago@email.com"));
    }

    @Test
    @DisplayName("Should return 400 when input data is invalid")
    void shouldReturn400WhenInputIsInvalid() throws Exception {
        CreateUserRequest request = new CreateUserRequest("", "invalid-email", "", "123");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.message").value(containsString("Erro de validação")));
    }
}
