package com.trebnikau.messenger.repo;

import com.trebnikau.messenger.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message,Integer > {

    List<Message> findAllByTag(String tag);
}
