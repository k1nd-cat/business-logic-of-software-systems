package io.blss.lab1.dto;

import io.blss.lab1.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Запрос на аутентификацию пользователя")
@Data
public class AuthRequest {
    @Schema(description = "Имя пользователя", example = "user123")
    private String username;

    @Schema(description = "Пароль", example = "password123")
    private String password;

    public User toUser() {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
