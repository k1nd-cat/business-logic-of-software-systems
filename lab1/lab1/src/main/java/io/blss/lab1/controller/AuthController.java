package io.blss.lab1.controller;

import io.blss.lab1.dto.AuthRequest;
import io.blss.lab1.dto.AuthResponse;
import io.blss.lab1.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("sign-up/user")
    public AuthResponse signUp(@RequestBody AuthRequest authRequest) {
        return authService.signUpUser(authRequest);
    }

    @PostMapping("sign-up/courier")
    public AuthResponse signUpCourier(@RequestBody AuthRequest authRequest) {
        return authService.signUpCourier(authRequest);
    }

    @PostMapping("sign-in")
    public AuthResponse signIn(@RequestBody AuthRequest authRequest) {
        return authService.signIn(authRequest);
    }
}
