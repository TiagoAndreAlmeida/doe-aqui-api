package br.com.doeaqui.infrastructure.gateways.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

    private UserEntityMapper userEntityMapper;
    private UserRepositoryGateway userRepositoryGateway;

    @BeforeEach
    void setUp() {
        userEntityMapper = new UserEntityMapper();
        userRepositoryGateway = new UserRepositoryGateway(userRepository, userEntityMapper);
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void shouldSaveUserSuccessfully() {
        User userDomain = new User();
        userDomain.setName("Domain User");
        userDomain.setEmail("test@test.com");

        UserEntity userEntity = userEntityMapper.toEntity(userDomain);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User result = userRepositoryGateway.createUser(userDomain);

        assertEquals(userDomain.getName(), result.getName());
    }

    @Test
    @DisplayName("Deve retornar true se e-mail existir")
    void shouldReturnTrueWhenEmailExists() {
        String email = "test@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = userRepositoryGateway.existsByEmail(email);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve retornar Optional com usuário quando encontrar por e-mail")
    void shouldReturnUserWhenEmailFound() {
        String email = "test@test.com";
        UserEntity entity = new UserEntity(1L, "Name", email, "123", "pass", false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        Optional<User> result = userRepositoryGateway.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(entity.getName(), result.get().getName());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando e-mail não encontrado")
    void shouldReturnEmptyWhenEmailNotFound() {
        String email = "ghost@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryGateway.findByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar Optional com usuário quando encontrar por ID")
    void shouldReturnUserWhenIdFound() {
        Long id = 1L;
        UserEntity entity = new UserEntity(id, "Name", "test@test.com", "123", "pass", false);
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<User> result = userRepositoryGateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(entity.getName(), result.get().getName());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando ID não encontrado")
    void shouldReturnEmptyWhenIdNotFound() {
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryGateway.findById(id);

        assertTrue(result.isEmpty());
    }
}
