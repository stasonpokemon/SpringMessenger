package com.trebnikau.messenger.repo;

import com.trebnikau.messenger.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

    User findUserByUsername(String userName);
    User findUserByActivationCode(String activationCode);
    User findUserByEmail(String email);
}
