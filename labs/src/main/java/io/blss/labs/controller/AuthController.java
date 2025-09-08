package io.blss.labs.controller;

import io.blss.labs.dto.AuthRequest;
import io.blss.labs.dto.AuthResponse;
import io.blss.labs.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Управление аутентификацией и регистрацией пользователей")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/users")
    @Operation(summary = "Регистрация пользователя")
    public AuthResponse registerUser(@Valid @RequestBody AuthRequest authRequest) {
        return authService.signUpUser(authRequest);
    }

    @PostMapping("/couriers")
    @Operation(summary = "Регистрация курьера")
    public AuthResponse registerCourier(@Valid @RequestBody AuthRequest authRequest) {
        return authService.signUpCourier(authRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.signIn(authRequest);
    }
}
