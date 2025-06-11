package io.blss.lab1.service;

import io.blss.lab1.dto.AuthRequest;
import io.blss.lab1.dto.AuthResponse;
import io.blss.lab1.entity.Role;
import io.blss.lab1.entity.ShoppingCart;
import io.blss.lab1.entity.User;
import io.blss.lab1.entity.XmlUser;
import io.blss.lab1.exception.RoleNotFoundException;
import io.blss.lab1.exception.UsernameAlreadyExistsException;
import io.blss.lab1.repository.RoleRepository;
import io.blss.lab1.repository.ShoppingCartRepository;
import io.blss.lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartRepository shoppingCartRepository;
//    private final XmlUserService xmlUserService;

    private static final String USER_ROLE_NAME = "USER_ROLE";
    private static final String COURIER_ROLE_NAME = "COURIER_ROLE";

    @Transactional
    public AuthResponse signUpUser(AuthRequest authRequest) {
        Role userRole = roleRepository.findByName(USER_ROLE_NAME)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + USER_ROLE_NAME));
        return signUp(authRequest, userRole);
    }

    @Transactional
    public AuthResponse signUpCourier(AuthRequest authRequest) {
        Role courierRole = roleRepository.findByName(COURIER_ROLE_NAME)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + COURIER_ROLE_NAME));
        return signUp(authRequest, courierRole);
    }

    public AuthResponse signIn(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Неверный логин или пароль", e);
        }

        final User user = userService.getByUsername(authRequest.getUsername());
        final String token = jwtService.generateToken(user);

        return AuthResponse.fromUser(user, token);
    }

    private AuthResponse signUp(AuthRequest authRequest, Role role) {
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Пользователь с таким именем уже существует: " + authRequest.getUsername());
        }

        User user = User.builder()
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .role(role)
                .build();

        user = userRepository.save(user);

//        xmlUserService.create(XmlUser.fromUser(user));

        final String token = jwtService.generateToken(user);

        if (USER_ROLE_NAME.equals(role.getName())) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            user.setShoppingCart(shoppingCart);
            shoppingCartRepository.save(shoppingCart);
        }

        return AuthResponse.fromUser(user, token);
    }
}
