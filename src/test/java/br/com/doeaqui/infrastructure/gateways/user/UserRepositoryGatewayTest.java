package br.com.doeaqui.infrastructure.gateways.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserRepositoryGatewayTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    private UserRepositoryGateway userRepositoryGateway;

    @BeforeEach
    void setUp() {
        userRepositoryGateway = new UserRepositoryGateway(userRepository, userEntityMapper);
    }

    @Test
    @DisplayName("Should orchestrate user creation correctly")
    void shouldOrchestrateUserCreation() {
        User userDomain = new User();
        userDomain.setName("Domain User");

        UserEntity userEntity = new UserEntity(null, "Entity User", "test@test.com", "123", "pass", false);
        UserEntity savedEntity = new UserEntity(1L, "Entity User", "test@test.com", "123", "pass", false);
        User savedDomain = new User();
        savedDomain.setId(1L);
        savedDomain.setName("Domain User");

        when(userEntityMapper.toEntity(userDomain)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedEntity);
        when(userEntityMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        User result = userRepositoryGateway.createUser(userDomain);

        assertEquals(savedDomain, result);
        verify(userEntityMapper).toEntity(userDomain);
        verify(userRepository).save(userEntity);
        verify(userEntityMapper).toDomain(savedEntity);
    }
}
