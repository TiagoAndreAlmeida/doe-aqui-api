package br.com.doeaqui.application.usecases.user;

import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;

public class CreateUserInterector {
    private UserGateway userGateway;

    public CreateUserInterector(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User createUser(User user) {
        return userGateway.createUser(user);
    }
}
