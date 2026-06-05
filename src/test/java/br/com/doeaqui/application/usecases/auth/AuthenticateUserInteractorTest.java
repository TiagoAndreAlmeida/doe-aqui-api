package br.com.doeaqui.application.usecases.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.doeaqui.application.gateways.password.PasswordEncoderGateway;
import br.com.doeaqui.application.gateways.token.TokenGateway;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserInteractorTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoderGateway passwordEncoderGateway;

    @Mock
    private TokenGateway tokenGateway;

    private AuthenticateUserInteractor authenticateUserInteractor;

    @BeforeEach
    void setUp() {
        authenticateUserInteractor = new AuthenticateUserInteractor(userGateway, passwordEncoderGateway, tokenGateway);
    }

    @Test
    @DisplayName("Deve autenticar com sucesso e retornar o token")
    void shouldAuthenticateSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String expectedToken = "valid.jwt.token";
        
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashed_password");
        user.setInactive(false);

        when(userGateway.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoderGateway.matches(password, "hashed_password")).thenReturn(true);
        when(tokenGateway.generateToken(user)).thenReturn(expectedToken);

        // Act
        String result = authenticateUserInteractor.authenticate(email, password);

        // Assert
        assertEquals(expectedToken, result);
        verify(userGateway).findByEmail(email);
        verify(passwordEncoderGateway).matches(password, "hashed_password");
        verify(tokenGateway).generateToken(user);
    }

    @Test
    @DisplayName("Deve lançar erro genérico quando usuário não for encontrado")
    void shouldThrowGenericExceptionWhenUserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        when(userGateway.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> authenticateUserInteractor.authenticate(email, "any_password"));
        
        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals("E-mail ou senha inválidos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar erro genérico quando a senha estiver incorreta")
    void shouldThrowGenericExceptionWhenPasswordIsIncorrect() {
        // Arrange
        String email = "test@example.com";
        String password = "wrong_password";
        
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashed_password");
        user.setInactive(false);

        when(userGateway.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoderGateway.matches(password, "hashed_password")).thenReturn(false);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> authenticateUserInteractor.authenticate(email, password));
        
        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals("E-mail ou senha inválidos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar erro genérico quando o usuário estiver inativo")
    void shouldThrowGenericExceptionWhenUserIsInactive() {
        // Arrange
        String email = "inactive@example.com";
        
        User user = new User();
        user.setEmail(email);
        user.setInactive(true);

        when(userGateway.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> authenticateUserInteractor.authenticate(email, "any_password"));
        
        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals("E-mail ou senha inválidos", exception.getMessage());
    }
}
