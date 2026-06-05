package br.com.doeaqui.infrastructure.gateways.user;

import java.util.Optional;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.persistence.user.UserRepository;

public class UserRepositoryGateway implements UserGateway {
    private UserRepository userRepository;
    private UserEntityMapper userEntityMapper;

    public UserRepositoryGateway(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User createUser(User userDomain) {
        return this.userEntityMapper.toDomain(userRepository.save(this.userEntityMapper.toEntity(userDomain)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::toDomain);
    }

}
