package com.danzki.repo;

import com.danzki.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
