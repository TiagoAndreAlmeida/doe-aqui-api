package br.com.doeaqui.application.usecases.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.doeaqui.application.gateways.password.PasswordEncoderGateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

@ExtendWith(MockitoExtension.class)
class CreateUserInteractorTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoderGateway passwordEncoderGateway;

    private CreateUserInteractor createUserInteractor;

    @BeforeEach
    void setUp() {
        createUserInteractor = new CreateUserInteractor(userGateway, passwordEncoderGateway);
    }

    @Test
    @DisplayName("Should call UserGateway and return the created user when email is unique")
    void shouldCallUserGatewayAndReturnCreatedUser() {
        User inputUser = new User();
        inputUser.setName("Test");
        inputUser.setEmail("test@email.com");
        inputUser.setPassword("password123");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Test");
        savedUser.setEmail("test@email.com");
        savedUser.setPassword("hashed_password");

        when(userGateway.existsByEmail("test@email.com")).thenReturn(false);
        when(passwordEncoderGateway.encode("password123")).thenReturn("hashed_password");
        when(userGateway.createUser(inputUser)).thenReturn(savedUser);

        User result = createUserInteractor.createUser(inputUser);

        assertEquals(savedUser, result);
        assertEquals("hashed_password", inputUser.getPassword());
        verify(userGateway).existsByEmail("test@email.com");
        verify(passwordEncoderGateway).encode("password123");
        verify(userGateway).createUser(inputUser);
    }

    @Test
    @DisplayName("Should throw BusinessExecption when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        User inputUser = new User();
        inputUser.setEmail("exists@email.com");

        when(userGateway.existsByEmail("exists@email.com")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> createUserInteractor.createUser(inputUser));
        assertEquals(ErrorCode.ALREADY_EXISTS, exception.getErrorCode());

        verify(userGateway).existsByEmail("exists@email.com");
        verify(passwordEncoderGateway, never()).encode(anyString());
        verify(userGateway, never()).createUser(inputUser);
    }
}
