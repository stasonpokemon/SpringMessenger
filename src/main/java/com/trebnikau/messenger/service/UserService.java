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

import java.util.*;
import java.util.stream.Collectors;

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
        } else if (userFromDBByEmail != null) {
            return 0;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        sendMessage(user);
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

    public Iterable<User> findAll() {
        Iterable<User> users = userRepo.findAll();
        return users;
    }

    public void saveUser(User user, String userName, Map<String, String> form) {
        user.setUsername(userName);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        // для начала нужно очистить все роли у user
        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = ((email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email)));
        if (isEmailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                // новый activate code для изера
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepo.save(user);

        // отпраляем юзеру на новую почту activate code
        if (isEmailChanged) {
            sendMessage(user);
        }

    }


    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s!/n" +
                            "Welcome to Messenger. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getUsername(), user.getActivationCode());
            mailSenderService.send(user.getEmail(), "Activation Code", message);
        }
    }
}
