package br.com.doeaqui.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import br.com.doeaqui.user.dto.request.CreateUserRequest;
import br.com.doeaqui.user.dto.request.LoginRequest;
import br.com.doeaqui.user.exception.EmailAlreadyExistsException;
import br.com.doeaqui.user.exception.InactiveUserException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve instanciar o serviço corretamente")
    void shouldInstantiateService() {
        assertThat(userService).isNotNull();
        assertThat(userService).isExactlyInstanceOf(UserService.class);
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso quando o e-mail não existe")
    void shouldCreateUserWithSuccess() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("Tiago", "tiago@email.com", "11999999999", "senha123");
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(any(String.class))).thenReturn("hash_senha");
        
        UserEntity expectedUser = new UserEntity(1L, request.name(), request.email(), request.phone(), "hash_senha", false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUser);

        // Act
        UserEntity createdUser = userService.create(request);

        // Assert
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getName()).isEqualTo(request.name());
        verify(userRepository).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com e-mail já cadastrado")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("Tiago", "duplicado@email.com", "", "senha123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.create(request))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessage("E-mail já cadastrado");

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer login com uma conta inativa")
    void shouldThrowExceptionWhenLoginWithInactiveUser() {
        LoginRequest request = new LoginRequest("inactive@email.com", "123456");
        Optional<UserEntity> user = Optional.ofNullable(new UserEntity(1L, "Usuário inativo", "inactive@email.com", "", "", true));
        
        when(userRepository.findByEmail(request.email())).thenReturn(user);

        assertThatThrownBy(() -> userService.authenticate(request))
            .isInstanceOf(InactiveUserException.class)
            .hasMessage("Conta inátiva, valide sua conta ou entre em contado com o suporte");
    }
}
