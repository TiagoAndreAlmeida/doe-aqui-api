package br.com.doeaqui.user;

import br.com.doeaqui.user.dto.request.CreateUserRequest;
import br.com.doeaqui.user.exception.EmailAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserEntity create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado");
        }

        String hashPassword = passwordEncoder.encode(request.password());

        UserEntity user = new UserEntity(
            null,
            request.name(),
            request.email(),
            request.phone() == null ? "" : request.phone(),
            hashPassword,
            true
        );

        return userRepository.save(user);
    }
}
