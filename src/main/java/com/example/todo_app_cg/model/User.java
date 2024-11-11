package com.example.todo_app_cg.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // Implement UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Replace with actual authorities if you have roles
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement based on your needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement based on your needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement based on your needs
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement based on your needs
    }
}
