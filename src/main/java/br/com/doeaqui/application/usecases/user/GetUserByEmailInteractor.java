package br.com.doeaqui.application.usecases.user;

import br.com.doeaqui.application.gateways.user.UserGateway;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.domain.execption.BusinessException;
import br.com.doeaqui.domain.execption.ErrorCode;

public class GetUserByEmailInteractor {
    private final UserGateway userGateway;

    public GetUserByEmailInteractor(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User getUserByEmail(String email) {
        return userGateway.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado", ErrorCode.NOT_FOUND));
    }
}
