package br.com.doeaqui.user;

import br.com.doeaqui.user.dto.request.LoginRequest;
import br.com.doeaqui.user.dto.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserEntity> me() {
        UserEntity user = (UserEntity) org.springframework.security.core.context.SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user);
    }
}
