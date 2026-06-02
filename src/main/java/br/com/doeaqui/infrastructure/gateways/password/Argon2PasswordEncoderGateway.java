package br.com.doeaqui.infrastructure.gateways.password;

import br.com.doeaqui.application.gateways.password.PasswordEncoderGateway;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Argon2PasswordEncoderGateway implements PasswordEncoderGateway {
    private final PasswordEncoder passwordEncoder;

    public Argon2PasswordEncoderGateway(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String RawPassword, String hashPassword) {
        return passwordEncoder.matches(RawPassword, hashPassword);
    }
    
}
