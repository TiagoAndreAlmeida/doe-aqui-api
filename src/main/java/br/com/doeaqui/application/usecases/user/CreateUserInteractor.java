package br.com.doeaqui.application.usecases.user;

import br.com.doeaqui.application.gateways.password.PasswordEncoderGateway;
import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

public class CreateUserInteractor {
    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;

    public CreateUserInteractor(UserGateway userGateway, PasswordEncoderGateway passwordEncoderGateway) {
        this.userGateway = userGateway;
        this.passwordEncoderGateway = passwordEncoderGateway;
    }

    public User createUser(User user) {
        if (userGateway.existsByEmail(user.getEmail())) {
            throw new BusinessException("E-mail já cadastrado", ErrorCode.ALREADY_EXISTS);
        }

        String hashPassword = passwordEncoderGateway.encode(user.getPassword());
        user.setPassword(hashPassword);

        return userGateway.createUser(user);
    }
}
