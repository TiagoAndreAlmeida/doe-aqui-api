package br.com.doeaqui.infrastructure.gateways.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.doeaqui.config.JwtService;
import br.com.doeaqui.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class JwtTokenGatewayTest {

    @Mock
    private JwtService jwtService;

    private JwtTokenGateway jwtTokenGateway;

    @BeforeEach
    void setUp() {
        jwtTokenGateway = new JwtTokenGateway(jwtService);
    }

    @Test
    @DisplayName("Deve chamar JwtService com os parâmetros corretos quando o usuário possuir ID")
    void shouldGenerateTokenWhenUserHasId() {
        // Arrange
        User user = new User();
        user.setId(123L);
        user.setEmail("user@example.com");
        user.setName("Test User");
        
        String expectedToken = "valid.jwt.token";
        when(jwtService.generateToken(123L, "user@example.com", "Test User")).thenReturn(expectedToken);

        // Act
        String result = jwtTokenGateway.generateToken(user);

        // Assert
        assertEquals(expectedToken, result);
        verify(jwtService).generateToken(123L, "user@example.com", "Test User");
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o usuário não possuir ID")
    void shouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        User user = new User();
        user.setId(null); // Usuário não persistido

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> jwtTokenGateway.generateToken(user));
        
        assertEquals("O usuário deve estar persistido (possuir ID) para gerar um token", exception.getMessage());
        verify(jwtService, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o objeto User for nulo")
    void shouldThrowExceptionWhenUserIsNull() {
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> jwtTokenGateway.generateToken(null));
        verify(jwtService, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Deve propagar exceção lançada pelo JwtService quando os dados são válidos")
    void shouldPropagateExceptionsFromJwtService() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(jwtService.generateToken(anyLong(), any(), any()))
            .thenThrow(new RuntimeException("Erro técnico interno"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jwtTokenGateway.generateToken(user));
    }
}
