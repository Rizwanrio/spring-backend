package com.example.todo_app_cg.security;

import com.example.todo_app_cg.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the authorities of the user
        return null; // Implement your logic to return authorities
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Adjust based on your needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Adjust based on your needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Adjust based on your needs
    }

    @Override
    public boolean isEnabled() {
        return true; // Adjust based on your needs
    }

    public User getUser() {
        return user; // Expose the original User object if needed
    }
}

