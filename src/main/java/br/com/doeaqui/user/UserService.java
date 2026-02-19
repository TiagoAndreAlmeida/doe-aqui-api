package br.com.doeaqui.user;

import br.com.doeaqui.user.dto.request.CreateUserRequest;
import br.com.doeaqui.user.exception.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado");
        }

        UserEntity user = new UserEntity(
            null,
            request.name(),
            request.email(),
            request.phone() == null ? "" : request.phone()
        );

        return userRepository.save(user);
    }
}
