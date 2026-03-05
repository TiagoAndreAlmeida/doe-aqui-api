package br.com.doeaqui.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class Argon2ConfigTest {

    private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 65536, 3);

    @Test
    @DisplayName("Deve gerar um hash Argon2id válido e conseguir validá-lo")
    void shouldEncodeAndMatchWithArgon2id() {
        String rawPassword = "minha_senha_segura";
        
        // Gera o hash
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // Verifica se o hash começa com o identificador do Argon2id ($argon2id$)
        assertThat(encodedPassword).startsWith("$argon2id$");
        
        // Verifica se a senha original bate com o hash
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
        
        // Verifica se uma senha errada não bate
        assertThat(passwordEncoder.matches("senha_errada", encodedPassword)).isFalse();
    }
}
