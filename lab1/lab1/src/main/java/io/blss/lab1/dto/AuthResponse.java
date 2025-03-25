package io.blss.lab1.dto;

import io.blss.lab1.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String username;
    private User.Role role;
    private String token;

    public static AuthResponse fromUser(User user, String token) {
        return AuthResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .build();
    }
}
