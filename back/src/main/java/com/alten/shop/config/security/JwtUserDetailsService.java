package com.alten.shop.config.security;

import com.alten.shop.config.AppProperties;
import com.alten.shop.model.User;
import com.alten.shop.repository.UserRepository;
import com.alten.shop.util.GlobalConstants;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AppProperties appProperties;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository, AppProperties appProperties) {
        this.userRepository = userRepository;
        this.appProperties = appProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(GlobalConstants.ROLE_USER));
        
        if (appProperties.getAdminEmail().equalsIgnoreCase(user.getEmail())) {
            authorities.add(new SimpleGrantedAuthority(GlobalConstants.ROLE_ADMIN));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
