package io.blss.lab1.dto;

import io.blss.lab1.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Ответ с данными аутентификации")
@Data
@Builder
public class AuthResponse {
    @Schema(description = "Имя пользователя", example = "user123")
    private String username;

    @Schema(description = "Роль пользователя", example = "USER")
    private User.Role role;

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public static AuthResponse fromUser(User user, String token) {
        return AuthResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .build();
    }
}
