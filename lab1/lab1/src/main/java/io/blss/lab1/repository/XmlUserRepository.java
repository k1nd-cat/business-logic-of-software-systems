package io.blss.lab1.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.blss.lab1.entity.XmlUser;
import io.blss.lab1.utils.UsersWrapper;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class XmlUserRepository {

    @Value("${user.data.file:users.xml}")
    private String dataFilePath;

    private final ObjectMapper xmlMapper = new XmlMapper();
    private List<XmlUser> users = new ArrayList<>();

    @PostConstruct
    public void loadUsers() {
        File file = new File(dataFilePath);
        if (file.exists()) {
            try {
                UsersWrapper wrapper = xmlMapper.readValue(file, UsersWrapper.class);
                if (wrapper != null && wrapper.getUsers() != null) {
                    this.users = wrapper.getUsers();
                }
                log.info("Загружено {} пользователей из файла: {}", this.users.size(), dataFilePath);
            } catch (IOException | java.io.IOException e) {
                log.error("Ошибка при загрузке пользователей из файла: {}", dataFilePath, e);
            }
        } else {
            log.info("Файл с данными пользователей не найден: {}", dataFilePath);
            saveUsers();
        }
    }

    private void saveUsers() {
        try {
            xmlMapper.writeValue(new File(dataFilePath), new UsersWrapper(this.users));
            log.info("Сохранено {} пользователей в файл: {}", this.users.size(), dataFilePath);
        } catch (IOException | java.io.IOException e) {
            log.error("Ошибка при сохранении пользователей в файл: {}", dataFilePath, e);
        }
    }

    public XmlUser save(XmlUser user) {
        Optional<XmlUser> existingUser = users.stream()
                .filter(u -> u.getUsername().equals(user.getUsername()))
                .findFirst();
        if (existingUser.isPresent()) {
            user.setId(existingUser.get().getId());
            users.remove(existingUser.get());
            users.add(user);
        } else {
            user.setId(generateId());
            users.add(user);
        }
        saveUsers();
        return user;
    }

    public boolean existsByUsername(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public Optional<XmlUser> findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    private Long generateId() {
        return users.isEmpty() ? 1L : users.stream().mapToLong(XmlUser::getId).max().orElse(0) + 1;
    }
}
