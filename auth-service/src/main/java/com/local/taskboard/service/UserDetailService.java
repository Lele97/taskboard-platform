package com.local.taskboard.service;

import com.local.taskboard.repository.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public UserDetailService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
