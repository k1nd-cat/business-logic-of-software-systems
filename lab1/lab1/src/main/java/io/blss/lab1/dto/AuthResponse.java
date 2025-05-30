package io.blss.lab1.dto;

import io.blss.lab1.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Ответ с данными аутентификации")
@Data
@Builder
public class AuthResponse {
    @Schema(description = "Имя пользователя", example = "user123")
    private String username;

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public static AuthResponse fromUser(User user, String token) {
        return AuthResponse.builder()
                .username(user.getUsername())
                .token(token)
                .build();
    }
}
