package io.blss.lab1.entity;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XmlUser implements UserDetails {

    private Long id;
    private String username;
    private String password;

    @XmlElement(name = "roleName")
    private String roleName;

    public static XmlUser fromUser(User user) {
        if (user == null) {
            return null;
        }
        XmlUserBuilder builder = XmlUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword());

        if (user.getRole() != null) {
            builder.roleName(user.getRole().getName());
        }
        return builder.build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roleName != null && !roleName.isEmpty()) {
            return List.of(new SimpleGrantedAuthority(roleName));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
