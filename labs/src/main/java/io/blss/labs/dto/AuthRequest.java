package io.blss.labs.dto;

import io.blss.labs.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Запрос на аутентификацию пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @Schema(description = "Имя пользователя", example = "user123")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 4, max = 50, message = "Имя пользователя должно быть от 4 до 50 символов")
    private String username;

    @Schema(description = "Пароль", example = "password123")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 5, max = 100, message = "Пароль должен быть от 5 до 100 символов")
    private String password;

    public User toUser() {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
