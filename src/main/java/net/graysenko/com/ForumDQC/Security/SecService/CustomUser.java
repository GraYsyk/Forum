package net.graysenko.com.ForumDQC.Security.SecService;

import net.graysenko.com.ForumDQC.Enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;


public class CustomUser implements UserDetails {
    private String username;
    private String password;
    private byte[] avatar;
    private Long id;  // Добавим ID
    private LocalDateTime createdAt;
    private UserStatus status;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUser(String username, String password, byte[] avatar, Long id, LocalDateTime createdAt, UserStatus status, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Long getId() {
        return id;  // Добавлен метод для получения ID
    }
}
