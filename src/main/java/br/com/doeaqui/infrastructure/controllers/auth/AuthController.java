package br.com.doeaqui.infrastructure.controllers.auth;

import br.com.doeaqui.application.usecases.auth.AuthenticateUserInteractor;
import br.com.doeaqui.infrastructure.controllers.auth.dto.AuthUserRequest;
import br.com.doeaqui.infrastructure.controllers.auth.dto.AuthUserResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUserInteractor authenticateUserInteractor;

    public AuthController(AuthenticateUserInteractor authenticateUserInteractor) {
        this.authenticateUserInteractor = authenticateUserInteractor;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserResponse> login(@RequestBody @Valid AuthUserRequest request) {
        String token = this.authenticateUserInteractor.authenticate(request.email(), request.password());
        AuthUserResponse response = new AuthUserResponse(token, "Bearer");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // @GetMapping("/me")
    // public ResponseEntity<CreateUserResponse> me(@AuthenticationPrincipal UserEntity principal) {
    //     UserEntity user = userService.findByEmail(principal.getEmail());
        
    //     CreateUserResponse response = new CreateUserResponse(
    //         user.getId(),
    //         user.getName(),
    //         user.getEmail(),
    //         user.getPhone(),
    //         user.getInactive(),
    //         user.getCreatedAt(),
    //         user.getUpdatedAt()
    //     );
        
    //     return ResponseEntity.ok(response);
    // }
}

