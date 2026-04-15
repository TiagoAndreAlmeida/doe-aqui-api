package br.com.doeaqui.user;

import br.com.doeaqui.infrastructure.controllers.user.dto.CreateUserResponse;
import br.com.doeaqui.infrastructure.persistence.user.UserEntity;
import br.com.doeaqui.user.dto.request.LoginRequest;
import br.com.doeaqui.user.dto.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CreateUserResponse> me(@AuthenticationPrincipal UserEntity principal) {
        UserEntity user = userService.findByEmail(principal.getEmail());
        
        CreateUserResponse response = new CreateUserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getInactive(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
        
        return ResponseEntity.ok(response);
    }
}
