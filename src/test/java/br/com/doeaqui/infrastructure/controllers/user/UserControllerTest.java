package br.com.doeaqui.infrastructure.controllers.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import java.time.LocalDateTime;

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

import br.com.doeaqui.config.SecurityConfig;
import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.exception.GlobalExceptionHandler;
import br.com.doeaqui.application.usecases.user.CreateUserInteractor;
import br.com.doeaqui.application.usecases.user.GetUserByEmailInteractor;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserDTOMapper;
import br.com.doeaqui.infrastructure.controllers.user.dto.request.CreateUserRequest;
import br.com.doeaqui.infrastructure.controllers.user.dto.response.CreateUserResponse;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserInteractor createUserInteractor;

    @MockitoBean
    private GetUserByEmailInteractor getUserByEmailInteractor;

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

    @Test
    @DisplayName("Should return 200 and user data when /me is called with authenticated user")
    void shouldReturnOkAndUserDataWhenMeIsCalled() throws Exception {
        String email = "tiago@email.com";
        UserEntity principal = new UserEntity(1L, "Tiago", email, "pass", "119999", false);
        
        User userDomain = new User();
        userDomain.setId(1L);
        userDomain.setEmail(email);
        userDomain.setName("Tiago");

        CreateUserResponse response = new CreateUserResponse(1L, "Tiago", email, "119999", false, LocalDateTime.now(), LocalDateTime.now());

        when(getUserByEmailInteractor.getUserByEmail(email)).thenReturn(userDomain);
        when(userDTOMapper.toResponse(userDomain)).thenReturn(response);

        mockMvc.perform(get("/users/me")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.name").value("Tiago"));
    }

    @Test
    @DisplayName("Should return 404 when user in token is not found")
    void shouldReturnNotFoundWhenUserInMeIsNotFound() throws Exception {
        String email = "ghost@email.com";
        UserEntity principal = new UserEntity(1L, "Ghost", email, "pass", "119999", false);

        when(getUserByEmailInteractor.getUserByEmail(email))
            .thenThrow(new BusinessException("Usuário não encontrado", ErrorCode.NOT_FOUND));

        mockMvc.perform(get("/users/me")
                .with(authentication(new UsernamePasswordAuthenticationToken(principal, null, null)))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }
}
