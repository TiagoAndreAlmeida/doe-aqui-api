package br.com.doeaqui.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve salvar um usuário com sucesso")
    void shouldSaveUser() {
        UserEntity user = new UserEntity(null, "João Silva", "joao@email.com", "11999999999", "senha", false);
        UserEntity savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("João Silva");
        assertThat(savedUser.getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("Não deve permitir salvar dois usuários com o mesmo e-mail")
    void shouldNotSaveDuplicateEmail() {
        // Cenário: Já existe um usuário com este e-mail no banco
        UserEntity user1 = new UserEntity(null, "Primeiro Usuário", "repetido@email.com", "", "senha", false);
        entityManager.persist(user1);
        entityManager.flush();

        // Ação: Tentar salvar outro usuário com o mesmo e-mail
        UserEntity user2 = new UserEntity(null, "Segundo Usuário", "repetido@email.com", "", "senha", false);

        // Verificação: Deve lançar DataIntegrityViolationException devido à constraint UNIQUE
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
            userRepository.flush(); // Força a ida ao banco para disparar a constraint
        });
    }

    @Test
    @DisplayName("Deve buscar um usuário pelo ID (Este teste deve falhar sem o construtor vazio)")
    void shouldFindUserById() {
        UserEntity user = new UserEntity(null, "Usuário Busca", "busca@email.com", "", "senha", true);
        UserEntity savedUser = userRepository.save(user);
        
        // Sincroniza com o banco e depois limpa a memória
        userRepository.flush();
        entityManager.clear();

        UserEntity foundUser = userRepository.findById(savedUser.getId()).orElseThrow();

        assertThat(foundUser.getName()).isEqualTo("Usuário Busca");
        assertThat(foundUser.getEmail()).isEqualTo("busca@email.com");
    }

    @Test
    @DisplayName("Deve retornar true quando o e-mail existir")
    void shouldReturnTrueWhenEmailExists() {
        String email = "existente@email.com";
        UserEntity user = new UserEntity(null, "Teste", email, "", "", true);
        entityManager.persist(user);
        entityManager.flush();

        boolean exists = userRepository.existsByEmail(email);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando o e-mail não existir")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        String email = "inexistente@email.com";

        boolean exists = userRepository.existsByEmail(email);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar usuário quando e-mail existir")
    void shouldReturnUserWhenEmailExist() {
        String email = "existence@email.com";
        UserEntity user = new UserEntity(null, "Teste", email, "", "", true);
        entityManager.persist(user);
        entityManager.flush();

        Optional<UserEntity> result = userRepository.findByEmail(email);
        
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Deve retornar vazio quando o e-mail não existir")
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        String email = "naoexistente@email.com";

        Optional<UserEntity> result = userRepository.findByEmail(email);
        
        assertThat(result).isEmpty();
    }
}
