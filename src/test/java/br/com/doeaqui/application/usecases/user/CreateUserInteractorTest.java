package br.com.doeaqui.application.usecases.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;

@ExtendWith(MockitoExtension.class)
class CreateUserInteractorTest {

    @Mock
    private UserGateway userGateway;

    private CreateUserInteractor createUserInteractor;

    @BeforeEach
    void setUp() {
        createUserInteractor = new CreateUserInteractor(userGateway);
    }

    @Test
    @DisplayName("Should call UserGateway and return the created user")
    void shouldCallUserGatewayAndReturnCreatedUser() {
        User inputUser = new User();
        inputUser.setName("Test");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Test");

        when(userGateway.createUser(inputUser)).thenReturn(savedUser);

        User result = createUserInteractor.createUser(inputUser);

        assertEquals(savedUser, result);
        verify(userGateway).createUser(inputUser);
    }
}
