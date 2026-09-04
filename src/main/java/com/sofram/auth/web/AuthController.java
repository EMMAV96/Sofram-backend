package com.sofram.auth.web;

import com.sofram.auth.application.AuthService;
import com.sofram.auth.application.UsuarioPrincipal;
import com.sofram.auth.web.dto.AuthResponse;
import com.sofram.auth.web.dto.CurrentUserResponse;
import com.sofram.auth.web.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> currentUser(@AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.ok(new CurrentUserResponse(principal.getId(), principal.getUsername(), principal.getRol()));
    }
}
