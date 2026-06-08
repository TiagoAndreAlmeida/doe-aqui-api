package br.com.doeaqui.application.gateways.user;

import java.util.Optional;
import br.com.doeaqui.domain.entity.User;

public interface UserGateway {
    User createUser(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
}
