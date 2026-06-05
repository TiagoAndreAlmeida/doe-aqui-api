package br.com.doeaqui.application.usecases.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
class GetUserByEmailInteractorTest {

    @Mock
    private UserGateway userGateway;

    private GetUserByEmailInteractor getUserByEmailInteractor;

    @BeforeEach
    void setUp() {
        getUserByEmailInteractor = new GetUserByEmailInteractor(userGateway);
    }

    @Test
    @DisplayName("Deve retornar o usuário quando o e-mail existir")
    void shouldReturnUserWhenEmailExists() {
        // Arrange
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        expectedUser.setName("Test User");

        when(userGateway.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = getUserByEmailInteractor.getUserByEmail(email);

        // Assert
        assertEquals(expectedUser, result);
        verify(userGateway).findByEmail(email);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando o gateway retornar vazio")
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        when(userGateway.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> getUserByEmailInteractor.getUserByEmail(email));
        
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userGateway).findByEmail(email);
    }
}
