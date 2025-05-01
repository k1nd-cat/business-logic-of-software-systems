package io.blss.lab1.service;

import io.blss.lab1.entity.XmlUser;
import io.blss.lab1.exception.UsernameAlreadyExistsException;
import io.blss.lab1.repository.XmlUserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class XmlUserService {

    private final XmlUserRepository userRepository; // Изменено на XmlUserRepository
    private final JwtService jwtService;

    public XmlUser save(XmlUser user) {
        return userRepository.save(user);
    }

    public XmlUser create(XmlUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Пользователь с таким именем уже существует");
        }
        return userRepository.save(user);
    }

    public XmlUser getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public XmlUser getCurrentUser() {
        var username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return getByUsername(username);
    }
}
