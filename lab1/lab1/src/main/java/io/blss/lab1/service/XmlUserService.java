//package io.blss.lab1.service;
//
//import io.blss.lab1.entity.XmlUser;
//import io.blss.lab1.exception.UsernameAlreadyExistsException;
//import io.blss.lab1.repository.XmlUserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class XmlUserService {
//
//    private final XmlUserRepository userRepository;
//
//    public XmlUser save(XmlUser user) {
//        return userRepository.save(user);
//    }
//
//    public XmlUser create(XmlUser user) {
//        if (userRepository.existsByUsername(user.getUsername())) {
//            throw new UsernameAlreadyExistsException("Пользователь с таким именем уже существует в XML хранилище: " + user.getUsername());
//        }
//        return userRepository.save(user);
//    }
//
//    public XmlUser getByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден в XML хранилище: " + username));
//    }
//
//    public UserDetailsService userDetailsService() {
//        return this::getByUsername;
//    }
//
//    public XmlUser getCurrentUser() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
//            throw new UsernameNotFoundException("Текущий пользователь не аутентифицирован или анонимен.");
//        }
//        String username = authentication.getName();
//        return getByUsername(username);
//    }
//}
