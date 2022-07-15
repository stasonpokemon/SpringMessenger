package com.trebnikau.messenger.service;

import com.trebnikau.messenger.entity.Role;
import com.trebnikau.messenger.entity.User;
import com.trebnikau.messenger.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

//    @Value("${server.port}")
//    private String serverPort;

    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findUserByUsername(username);
    }

    public int addUser(User user, String email) {
        User userFromDBByUsername = userRepo.findUserByUsername(user.getUsername());
        User userFromDBByEmail = userRepo.findUserByEmail(email);
        if (userFromDBByUsername != null) {
            return -1;
        }else if(userFromDBByEmail != null){
            return  0;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s!/n" +
                            "Welcome to Messenger. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailSenderService.send(user.getEmail(), "Activation Code", message);
        }
        return 1;
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
