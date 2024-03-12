package com.danzki.repo;

import com.danzki.model.Login;
import org.springframework.data.repository.CrudRepository;

public interface LoginRepo extends CrudRepository<Login, Long> {
}
