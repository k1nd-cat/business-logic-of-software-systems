package io.blss.lab1.service;

import io.blss.lab1.dto.AuthRequest;
import io.blss.lab1.dto.AuthResponse;
import io.blss.lab1.entity.ShoppingCart;
import io.blss.lab1.entity.User;
import io.blss.lab1.exception.UsernameAlreadyExistsException;
import io.blss.lab1.repository.ShoppingCartRepository;
import io.blss.lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartRepository shoppingCartRepository;

    public AuthResponse signUpUser(AuthRequest authRequest) {
        return signUp(authRequest, User.Role.ROLE_USER);
    }

    public AuthResponse signUpCourier(AuthRequest authRequest) {
        return signUp(authRequest, User.Role.ROLE_COURIER);
    }

    public AuthResponse signIn(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Неверный логин или пароль", e);
        }

        final var user = userService.getByUsername(authRequest.getUsername());
        final var token = jwtService.generateToken(user);

        return AuthResponse.fromUser(user, token);
    }

    private AuthResponse signUp(AuthRequest authRequest, User.Role role) {
        if (userRepository.existsByUsername(authRequest.getUsername()))
            throw new UsernameAlreadyExistsException(authRequest.getUsername());

        var user = authRequest.toUser();
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userService.save(user);
        final var token = jwtService.generateToken(user);

        if (role == User.Role.ROLE_USER) {
            var shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);
        }

        return AuthResponse.fromUser(user, token);
    }
}

