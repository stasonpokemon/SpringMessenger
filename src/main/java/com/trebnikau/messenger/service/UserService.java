package com.trebnikau.messenger.service;

import com.trebnikau.messenger.entity.Role;
import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findUserByUsername(username);
    }

    public boolean addUser(User user, String email) {
        User userFromDBByUsername = userRepo.findUserByUsername(user.getUsername());
        User userFromDBByEmail = userRepo.findUserByEmail(email);
        if (userFromDBByUsername != null || userFromDBByEmail != null) {
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s!/n" +
                            "Welcome to Messenger. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailSenderService.send(user.getEmail(), "Activation Code", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findUserByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);
        return true;
    }
}
