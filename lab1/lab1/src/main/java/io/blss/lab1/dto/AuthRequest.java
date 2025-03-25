package io.blss.lab1.dto;

import io.blss.lab1.entity.User;
import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

    public User toUser() {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
