package br.com.doeaqui.application.gateways.user;

import br.com.doeaqui.domain.entity.User;

public interface UserGateway {
    User createUser(User user);
}
