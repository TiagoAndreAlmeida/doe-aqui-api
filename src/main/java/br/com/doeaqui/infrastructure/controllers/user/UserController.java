package br.com.doeaqui.infrastructure.controllers.user;

import br.com.doeaqui.application.usecases.user.CreateUserInteractor;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.controllers.user.dto.CreateUserRequest;
import br.com.doeaqui.infrastructure.controllers.user.dto.CreateUserResponse;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserDTOMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserInteractor createUserInterector;
    private final UserDTOMapper userDTOMapper;

    public UserController(CreateUserInteractor createUserInterector, UserDTOMapper userDTOMapper) {
        this.createUserInterector = createUserInterector;
        this.userDTOMapper = userDTOMapper;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        User userDomain = this.userDTOMapper.toDomain(request);
        User createdUser = this.createUserInterector.createUser(userDomain);
        CreateUserResponse response = this.userDTOMapper.toResponse(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
