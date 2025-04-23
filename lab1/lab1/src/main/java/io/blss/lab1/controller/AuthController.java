package io.blss.lab1.controller;

import io.blss.lab1.dto.AuthRequest;
import io.blss.lab1.dto.AuthResponse;
import io.blss.lab1.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
