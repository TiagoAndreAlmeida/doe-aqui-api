package br.com.doeaqui.application.usecases.user;

import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;

public class CreateUserInteractor {
    private UserGateway userGateway;

    public CreateUserInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User createUser(User user) {
        return userGateway.createUser(user);
    }
}
