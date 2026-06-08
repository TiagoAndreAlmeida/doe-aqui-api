package br.com.doeaqui.infrastructure.controllers.user;

import br.com.doeaqui.application.usecases.user.CreateUserInteractor;
import br.com.doeaqui.application.usecases.user.GetUserByEmailInteractor;
import br.com.doeaqui.domain.entity.User;
import br.com.doeaqui.infrastructure.controllers.user.dto.UserDTOMapper;
import br.com.doeaqui.infrastructure.controllers.user.dto.request.CreateUserRequest;
import br.com.doeaqui.infrastructure.controllers.user.dto.response.CreateUserResponse;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserInteractor createUserInteractor;
    private final GetUserByEmailInteractor getUserByEmailInteractor;
    private final UserDTOMapper userDTOMapper;

    public UserController(CreateUserInteractor createUserInteractor, GetUserByEmailInteractor getUserByEmailInteractor, UserDTOMapper userDTOMapper) {
        this.createUserInteractor = createUserInteractor;
        this.getUserByEmailInteractor = getUserByEmailInteractor;
        this.userDTOMapper = userDTOMapper;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        User userDomain = this.userDTOMapper.toDomain(request);
        User createdUser = this.createUserInteractor.createUser(userDomain);
        CreateUserResponse response = this.userDTOMapper.toResponse(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CreateUserResponse> getMe(@AuthenticationPrincipal UserEntity principal) {
        User userDomain = this.getUserByEmailInteractor.getUserByEmail(principal.getEmail());
        CreateUserResponse response = this.userDTOMapper.toResponse(userDomain);
        return ResponseEntity.ok(response);
    }
}
