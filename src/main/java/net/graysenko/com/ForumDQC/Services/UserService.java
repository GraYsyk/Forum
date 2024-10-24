package net.graysenko.com.ForumDQC.Services;

import net.graysenko.com.ForumDQC.Models.UserINF;
import net.graysenko.com.ForumDQC.Repositories.UserINFRepository;
import net.graysenko.com.ForumDQC.Security.SecService.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserINFRepository userinfRepository;

    @Autowired
    public UserService(UserINFRepository userinfRepository) {
        this.userinfRepository = userinfRepository;
    }

    public Optional<UserINF> getUserByUsername(String username) {
        return userinfRepository.findByUsername(username);
    }

    public UserINF getUserById(long id) {
        return userinfRepository.findById(id);
    }

    public byte[] getUserAvatar(long id) {
        UserINF user = getUserById(id);
        return (user != null) ? user.getAvatar() : null;
    }

    public void saveUser(UserINF user) {
        userinfRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserINF user = getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User %s not found", username)));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole()));

        // Создание вашего кастомного пользователя
        return new CustomUser(
                user.getUsername(),
                user.getPassword(),
                user.getAvatar(),
                user.getId(),
                user.getCreatedAt(),
                user.getStatus(),
                authorities
        );
    }
}
